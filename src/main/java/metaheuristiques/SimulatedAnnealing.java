package metaheuristiques;
import Nurse_Restoring.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static metaheuristiques.FixAndOptimize.fixAndOptimize;
/**
 * La classe SimulatedAnnealing implémente l'algorithme du recuit simulé pour résoudre un problème d'optimisation.
 * Elle cherche à trouver la meilleure solution en explorant l'espace des solutions à différentes températures,
 * en acceptant les solutions qui améliorent le coût actuel ou en fonction d'une probabilité d'acceptation dépendant
 * de la différence de coût et de la température.
 *
 * L'algorithme ajuste la température pour contrôler l'exploration et l'exploitation de l'espace des solutions.
 * Il effectue également une optimisation locale en appliquant une heuristique spécifique (fixAndOptimize)
 * lorsque la recherche locale n'améliore pas la solution pendant un certain nombre d'itérations (jMax).
 *
 * La classe Solution est définie en interne et doit être adaptée au contexte du problème spécifique.
 * Elle doit fournir des méthodes pour calculer le coût de la solution actuelle (getCost) et pour générer
 * une solution voisine (generateNeighbor).
 *
 *
 * @author ELMOUDEN et SEKKOUMI
 */

public class SimulatedAnnealing {

    private double temperature;
    private double coolingRate;
    private int maxIterations;
    private Solution currentSolution;
    private Solution bestSolution;
    private int jMax;

    public SimulatedAnnealing(double temperature, double coolingRate, int maxIterations, int jMax) {
        this.temperature = temperature;
        this.coolingRate = coolingRate;
        this.maxIterations = maxIterations;
        this.jMax = jMax;
    }

    public Solution findOptimum(Solution initialSolution) {
        currentSolution = initialSolution;
        bestSolution = initialSolution;

        Random random = new Random();

        while (temperature > 1) {
            int j = 0;
            for (int i = 0; i < maxIterations && j < jMax; i++) {
                Solution newSolution = currentSolution.generateNeighbor();

                double currentEnergy = currentSolution.getCost();
                double neighbourEnergy = newSolution.getCost();
                double delta = neighbourEnergy - currentEnergy;

                if (delta < 0 || random.nextDouble() < Math.exp(-delta / temperature)) {
                    currentSolution = newSolution;
                    j = 0; // Réinitialiser le compteur j si une nouvelle solution est acceptée
                } else {
                    j++; // Incrémenter le compteur j si la nouvelle solution n'est pas acceptée
                }

                if (currentSolution.getCost() < bestSolution.getCost()) {
                    bestSolution = currentSolution;
                }
            }

            if (j >= jMax) {
                currentSolution = fixAndOptimize(currentSolution);
                j = 0; // Réinitialiser le compteur j après l'optimisation
            }

            temperature *= coolingRate;
        }

        return bestSolution;
    }

    private double acceptanceProbability(double currentEnergy, double newEnergy, double temperature) {
        if (newEnergy < currentEnergy) {
            return 1.0;
        }
        return Math.exp((currentEnergy - newEnergy) / temperature);
    }

    // Define the Solution class and its methods getCost and generateNeighbor as per your problem's context
    public static class Solution {
        private List<Shift> assignedShifts = new ArrayList<>();
        private Random random = new Random();
        public double getCost() {
            double cost = 0;
            // Exemple : calculer le coût basé sur le nombre d'heures supplémentaires
            for (Shift shift : assignedShifts) {
                cost += calculateExtraHours(shift);
            }
            // Ajouter d'autres éléments de coût si nécessaire
            return cost;
        }

        private double calculateExtraHours(Shift shift) {
            // Implémentez la logique pour calculer les heures supplémentaires
            return 0; // Exemple
        }



        public Solution generateNeighbor() {
            Solution neighbor = new Solution(); // Créer une copie de la solution actuelle
            // Exemple : échanger deux quarts de travail entre deux employés
            int shiftIndex1 = random.nextInt(assignedShifts.size());
            int shiftIndex2 = random.nextInt(assignedShifts.size());
            Collections.swap(neighbor.assignedShifts, shiftIndex1, shiftIndex2);
            return neighbor;
        }

    }

    public static void main(String[] args) {
        // Define your initial solution
        Solution initialSolution = new Solution();

        // Create an instance of the metaheuristiques.SimulatedAnnealing class with appropriate parameters
        SimulatedAnnealing sa = new SimulatedAnnealing(10000, 0.003, 100,50);

        // Find the optimum solution
        Solution optimum = sa.findOptimum(initialSolution);

        // Output the result
        System.out.println("Optimum solution cost: " + optimum.getCost());
    }
}
