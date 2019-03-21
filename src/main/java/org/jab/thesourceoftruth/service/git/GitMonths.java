package org.jab.thesourceoftruth.service.git;

import java.util.HashMap;
import java.util.Map;

public enum GitMonths {

    Jan(1),
    Feb(2),
    Mar(3),
    Apr(4),
    May(5),
    Jun(6),
    Jul(7),
    Ago(8),
    Sep(9),
    Oct(10),
    Nov(11),
    Dec(12);

    private final int value;

    GitMonths(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }

    // Mapping difficulty to difficulty id
    private static final Map<Integer, GitMonths> _map = new HashMap<Integer, GitMonths>();
    static
    {
        for (GitMonths difficulty : GitMonths.values())
            _map.put(difficulty.getValue(), difficulty);
    }

    /**
     * Get difficulty from value
     * @param value Value
     * @return GitMonths
     */
    public static GitMonths from(int value) {
        return _map.get(value);
    }
}