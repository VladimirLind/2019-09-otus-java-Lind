package ru.otus.atm;

import java.util.EnumMap;
import java.util.EnumSet;

class Cells {

    public enum Nominals {
        TEN(10),
        FIFTY(50),
        HUNDRED(100),
        THOUSAND(1000);

        private final Integer label;

        Nominals(int label) {
            this.label = label;
        }

        static Nominals getEnumByInt(int value) {
            for (Nominals e : Nominals.values()) {
                if (value == e.getLabel()) return e;
            }
            return null;
        }

        int getLabel() {
            return label;
        }
    }

    final EnumMap cellsmap;

    final int nominalscount;

    public EnumMap getCellsmap() {
        return cellsmap;
    }

    Cells(int nominalscount) {
        this.cellsmap = createCells(nominalscount);
        this.nominalscount = nominalscount;
    }

    private EnumMap createCells(int nominalscount) {
        EnumMap<Nominals, Integer> cellsmap = new EnumMap<>(Nominals.class);
        EnumSet.allOf(Nominals.class).forEach(nominal -> cellsmap.put(nominal, nominalscount));
        return cellsmap;
    }
}
