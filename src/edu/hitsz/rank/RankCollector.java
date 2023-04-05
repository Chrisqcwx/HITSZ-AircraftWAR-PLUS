package edu.hitsz.rank;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.utils.Params;
import edu.hitsz.utils.params.PathParams;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

public class RankCollector {

    private final Queue<RankDataStruct> data = new PriorityQueue<RankDataStruct>(
        (d1, d2) -> d2.score() - d1.score()
    );

    private final Path path = Path.of(PathParams.data);

    private RankCollector() throws IOException {
        if(Files.exists(path)) {
            List<String> values = Files.readAllLines(path);
            values.forEach(value -> {
                Optional.of(RankDataStruct.parseRankData(value))
                        .ifPresent(data::add);
            });
        }
    }

    private static volatile RankCollector collector = null;

    public static RankCollector getInstance() throws IOException {
        if (collector == null) {
            synchronized (RankCollector.class) {
                if (collector == null) {
                    collector = new RankCollector();
                }
            }
        }
        return collector;
    }

    public void add(String player, int score) {
        Date time = new Date();
        data.add(new RankDataStruct(player, score, time));
    }

    public void clearRank() {
        data.clear();
    }

    public void save() throws IOException {
        String writeString = String.join("\r\n", data.stream().map(RankDataStruct::toString).toList());
        Files.writeString(path, writeString);
    }

    @Override
    public String toString() {
        return String.join("\r\n", IntStream.range(0, data.size()).mapToObj(i -> (new StringBuffer())
                .append("rank ")
                .append(i + 1)
                .append(": ")
                .append(data.poll())
                .toString()).toList()
        );
    }
}
