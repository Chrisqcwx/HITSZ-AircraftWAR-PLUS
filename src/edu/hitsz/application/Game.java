package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.aircraft.enemyaircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.enemyaircraft.BossEnemy;
import edu.hitsz.aircraft.enemyaircraft.factory.AbstractEnemyFactory;
import edu.hitsz.aircraft.enemyaircraft.factory.RandomEnemyFactoryGenerator;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.BaseProp;
import edu.hitsz.rank.RankCollector;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class Game extends JPanel {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<AbstractEnemyAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<BaseProp> props;

    /**
     * 屏幕中出现的敌机最大数量
     */
    private int enemyMaxNumber = 5;

    /**
     * 当前得分
     */
    private int score = 0;
    /**
     * 当前时刻
     */
    private int time = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;

    /**
     * 游戏结束标志
     */
    private boolean gameOverFlag = false;

    /**
     * 随机选择生成器
     */
    private RandomEnemyFactoryGenerator enemyFactorySeletor;

    /**
     * 数据库
     */
    RankCollector collector;

    public Game() throws IOException {
        collector = RankCollector.getInstance();

        heroAircraft = HeroAircraft.getInstance();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

        // 设置随机生成器
        List<Float> weights = Arrays.asList(70f,30f);
        this.enemyFactorySeletor = new RandomEnemyFactoryGenerator(weights);
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                // 新敌机产生
                createEnemy();
                // 飞机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // Prop移动
            propsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            //每个时刻重绘界面
            repaint();

            // 游戏结束检查英雄机是否存活
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                executorService.shutdown();
                gameOverFlag = true;
                System.out.println("Game Over!");

                collector.add("Chris", score);
                try {
                    collector.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("排行榜");
                System.out.println(collector.toString());
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean hasBoss = false;
    private int tryCreateBossNum = 0;
    private final int createBossScore = 600;
    private boolean isCreateBoss() {
        if (score/createBossScore-tryCreateBossNum>=1){
            tryCreateBossNum+=1;
            if(hasBoss) {
                return false;
            }
            else {
                hasBoss = true;
                return true;
            }
        }
        return false;
    }

    private void createEnemy() {
        if (enemyAircrafts.size() < enemyMaxNumber) {
            AbstractEnemyFactory enemyFactory = enemyFactorySeletor.nextEnemyFactory(isCreateBoss());
            if(enemyFactory == null) {
                return;
            }
            AbstractEnemyAircraft enemy = enemyFactory.createEnemy();
            if (enemy != null) {
                enemyAircrafts.add(enemy);
            }
        }
    }



    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // 敌机射击
        enemyAircrafts.forEach(enemyAircraft->{
            enemyBullets.addAll(enemyAircraft.shoot());
        });
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    private void bulletsMoveAction() {
        heroBullets.forEach(BaseBullet::forward);
        enemyBullets.forEach(BaseBullet::forward);
    }

    private void propsMoveAction() {
//        for (BaseProp prop : props) {
//            prop.forward();
//        }
        props.forEach(BaseProp::forward);
    }

    private void aircraftsMoveAction() {
//        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
//            enemyAircraft.forward();
//        }
        enemyAircrafts.forEach(AbstractAircraft::forward);
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        //  敌机子弹攻击英雄
        enemyBullets.stream()
                .filter(bullet -> this.crashCheck(this.heroAircraft, bullet))
                .forEach(bullet -> {
                    this.heroAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                });


        // 英雄子弹攻击敌机
        enemyAircrafts.stream()
                .peek(enemyAircraft -> heroBullets.stream()
                    .filter(bullet -> crashCheck(enemyAircraft, bullet))
                    .forEach(bullet -> {
                        enemyAircraft.decreaseHp(bullet.getPower());
                        bullet.vanish();
                    })
                )
                // 摧毁敌机，掉落道具
                .filter(AbstractEnemyAircraft::notValid)
                .peek(this::reward)
                // 判断摧毁Boss
                .filter(enemy -> enemy instanceof BossEnemy)
                .forEach(boss -> hasBoss = false);

        // 英雄机 与 敌机 相撞，均损毁
        enemyAircrafts.stream()
                .filter(enemyAircraft -> (crashCheck(enemyAircraft, heroAircraft) ||
                        crashCheck(heroAircraft, enemyAircraft)))
                .forEach(enemyAircraft -> {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                });


        // 我方获得道具，道具生效
        this.props.stream()
                .filter(prop -> crashCheck(this.heroAircraft, prop))
                .forEach(prop -> {
                    prop.effect(this.heroAircraft);
                    prop.vanish();
                });
    }

    private boolean crashCheck(AbstractFlyingObject src, AbstractFlyingObject dst) {
        return !src.notValid() && !dst.notValid() && src.crash(dst);
    }

    private void reward(ICreateReward enemy) {
        // TODO 获得分数，产生道具补给
        this.score += enemy.getScore();
        List<BaseProp> props = enemy.getProps();
        props.stream()
                .filter(Objects::nonNull)
                .forEach(this.props::add);
    }


    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, props);

        paintImageWithPositionRevised(g, enemyAircrafts);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }


}
