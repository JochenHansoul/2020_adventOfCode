package day07.bagutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

public class Bags {
    HashSet<Bag> bags;

    public Bags(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            this.bags = new HashSet<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parentAndChildren = line.split(" bags contain ");
                String[] parentBag = parentAndChildren[0]
                        .split(" ");
                // current bag
                Bag currentBag = new Bag(
                        Pattern.valueOf(parentBag[0].toUpperCase()),
                        Color.valueOf(parentBag[1].toUpperCase()));
                currentBag = addBagToListOrGetOriginalBag(currentBag);
                // child bags
                if (!parentAndChildren[1].equals("no other bags.")) {
                    HashMap<Bag, Integer> currentBagContent = new HashMap<>(); // don't forget to create a new object!
                    String[] childBags = parentAndChildren[1]
                            .substring(0, parentAndChildren[1].length() - 1) // removing last "."
                            .replaceAll(" bag[s]", "")
                            .split(", ");
                    for (String childBagString : childBags) {
                        String[] childBagArray = childBagString.split(" ");
                        Bag childBag = new Bag(
                                Pattern.valueOf(childBagArray[1].toUpperCase()),
                                Color.valueOf(childBagArray[2].toUpperCase()));
                        childBag = addBagToListOrGetOriginalBag(childBag);
                        currentBag.content.put(childBag, Integer.parseInt(childBagArray[0]));
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public Bag getBag(Pattern pattern, Color color) {
        return getOriginalBag(new Bag(pattern, color));
    }

    private Bag addBagToListOrGetOriginalBag(Bag bag) {
        if (this.bags.contains(bag)) {
            bag = getOriginalBag(bag);
        } else {
            this.bags.add(bag);
        }
        return bag;
    }

    private Bag getOriginalBag(Bag bag) {
        Bag[] arrayBags = this.bags.toArray(new Bag[0]);
        int i = 0;
        boolean found = false;
        while (!found) {
            if (arrayBags[i].equals(bag)) {
                bag = arrayBags[i];
                found = true;
            } else {
                i++;
            }
        }
        return bag;
    }
}