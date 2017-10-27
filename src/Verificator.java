import template.*;

public class Verificator {

    public static boolean hasUniqueInfoTable(Parking parking) {
        int count = 0;
        for (FunctionalBlock[] blocks : parking.getParking()) {
            for (FunctionalBlock block : blocks) {
                if (block instanceof InfoTable) {
                    count++;
                    if (count > 1)
                        return false;
                }
            }
        }
        return count == 1;
    }

    public static boolean hasUniqueCashBox(Parking parking) {
        int count = 0;
        for (FunctionalBlock[] blocks : parking.getParking()) {
            for (FunctionalBlock block : blocks) {
                if (block instanceof CashBox) {
                    count++;
                    if (count > 1)
                        return false;
                }
            }
        }
        return count == 1;
    }

    public static boolean isEntryDeparturePlacedCorrect(Parking parking) {
        int countEntry = 0, countDeparture = 0;
        int jEntry = 0, iEntry = 0, jDeparture = 0, iDeparture = 0;
        for (int i = 0; i < parking.getFunctionalBlockH(); i++) {
            for (int j = 0; j < parking.getFunctionalBlockV(); j++) {
                if (parking.getFunctionalBlock(i, j) instanceof Entry) {
                    countEntry++;
                    iEntry = i;
                    jEntry = j;
                    if (countEntry > 1)
                        return false;
                }
            }
        }
        for (int i = 0; i < parking.getFunctionalBlockH(); i++) {
            for (int j = 0; j < parking.getFunctionalBlockV(); j++) {
                if (parking.getFunctionalBlock(i, j) instanceof Departure) {
                    countDeparture++;
                    iDeparture = i;
                    jDeparture = j;
                    if (countDeparture > 1)
                        return false;
                }
            }
        }
        return ((countEntry == 1) && (countDeparture == 1) && (iEntry < iDeparture)
                && (jEntry == parking.getFunctionalBlockV() - 1)
                && (jDeparture == parking.getFunctionalBlockV()));
    }


}
