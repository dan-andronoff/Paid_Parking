package parking;

import graph.Graph;
import graph.Node;
import parking.template.*;

import java.util.ArrayList;

public class Verificator {

    public static ArrayList<VerificatorError> checkAll(Parking parking) {
        ArrayList<VerificatorError> errors = new ArrayList<>();
        if (!isEntryDeparturePlacedCorrect(parking)) {
            errors.add(VerificatorError.IncorrectEntryDeparturePlacement);
        }
        if (!checkPaths(parking)) {
            errors.add(VerificatorError.UnrelatedGraph);
        }
        if (!hasCashBox(parking)){
            errors.add(VerificatorError.hasNoCashBox);
        }
        if (!hasInfoTable(parking)){
            errors.add(VerificatorError.hasNoInfoTable);
        }
        return errors;
    }

    private static boolean hasCashBox(Parking parking){
        return (parking.getCashBoxI()!=-1);
    }

    private static boolean hasInfoTable(Parking parking){
        return (parking.getInfoTableI()!=-1);
    }

    private static boolean isEntryDeparturePlacedCorrect(Parking parking) {
        return (parking.getEntryI() < parking.getDepartureI())
                && (parking.getEntryJ() == parking.getFunctionalBlockV() - 1)
                && (parking.getDepartureJ() == parking.getFunctionalBlockV()-1);
    }

    private static boolean checkPaths(Parking parking){
        Graph graph = new Graph(parking);
        if (graph.getEntry()!=null) {
            for (Node node : graph) {
                if (!graph.isReachable(graph.getEntry(), node)) {
                    return false;
                }
            }
            return true;
        }
        else return false;
    }
}
