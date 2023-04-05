package edu.hitsz.rank;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public record RankDataStruct(String player, int score, Date time) {

    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static RankDataStruct parseRankData(String dataString) {
        if(dataString==null || dataString.isBlank()){
            return null;
        }
        String[] values = dataString.split(",");
        if(values.length != 3) {
            throw new RuntimeException("the length of data is not equal to 3");
        }
        int score=-1;
        try {
            score = Integer.parseInt(values[1]);
        }catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String player = values[0];
        Date time = null;
        try {
            time = format.parse(values[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new RankDataStruct(player, score, time);
    }

    @Override
    public String toString() {
        return String.join(",", player,
                String.valueOf(score), format.format(time));
    }
}
