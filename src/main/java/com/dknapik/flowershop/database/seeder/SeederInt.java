package com.dknapik.flowershop.database.seeder;

/**
 * Used to populated database with exemplary data to assist in debugging and development.
 *
 * @author Damian
 */
public interface SeederInt {

    /**
     * Main method where objects are instantiated and saved through repository into database.
     */
    public void seed();

    /**
     * Some data might be crucial for application and need to always be in database.
     * For example, we need at least one account with admin privileges to let owner of the application manage
     * it from the application gui.
     *
     * @see AccountSeeder
     * @return true if application should invoke data only in debug/development mode
     */
    public boolean isOnlyForDebug();

}
