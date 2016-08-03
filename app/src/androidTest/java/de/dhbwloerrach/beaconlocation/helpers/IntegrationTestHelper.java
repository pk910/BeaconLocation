package de.dhbwloerrach.beaconlocation.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.Machine;

/**
 * Created by David on 7/24/15.
 * Implements a set of functions used for other testings classes
 */
public class IntegrationTestHelper {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") // Could be looked into, but shouldn't hurt the test
    public List<Beacon> createBeacons(int count)
    {
        List<Beacon> beacons = new ArrayList<>(count);
        List<Integer> majorIds = new ArrayList<>();
        List<Integer> minorIds = new ArrayList<>();

        for (int index = 0; index < count; index ++) {
            Integer majorId;
            Integer minorId;

            do {
                majorId = createRandom(1,9999);
            } while (majorIds.contains(majorId));
            do {
                minorId = createRandom(1,9999);
            } while (minorIds.contains(minorId));

            Beacon beacon = new de.dhbwloerrach.beaconlocation.models.Beacon();
            beacon.setUuid("01234567-89AB-CDEF-0123-000000000001")
                    .setMajor(majorId)
                    .setMinor(minorId)
                    .setBluetoothName("")
                    .setBluetoothAddress("")
                    .setDistance(createRandom(1d, 10))
                    .setTxpower(0)
                    .setRssi(0);

            beacons.add(beacon);
        }

        return beacons;
    }

    @SuppressWarnings("SameParameterValue")
    public List<Machine> createMachines(int count) {
        List<Machine> machines = new ArrayList<>(count);

        for (int index = 0; index < count; index ++) {
            int id = createRandom(1, 9999);

            Machine machine = new Machine();
            machine.setId(id);
            machine.setName("Machine " + id);

            machines.add(machine);
        }

        return machines;
    }

    public int createRandom(int min, int max)
    {
        int range = Math.abs(max - min) + 1;
        return (int)(Math.random() * range) + (min <= max ? min : max);
    }

    @SuppressWarnings("SameParameterValue")
    public double createRandom(double min, double max)
    {
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }
}
