package simpleAuctions.auction;

import simpleAuctions.Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;

public class LoadAuctionFromDisk {
    private static LoadAuctionFromDisk instance;
    public LoadAuctionFromDisk() {
    }

    public static synchronized LoadAuctionFromDisk getInstance() {
        if (instance == null) {
            instance = new LoadAuctionFromDisk();
        }
        return instance;
    }

    private static final String COMMA_SEPARATOR = ",";
    private static final int AUCTION_TITLE = 0;
    private static final int AUCTION_DESCRIPTION = 1;
    private static final int AUCTION_CATEGORY = 2;
    private static final int AUCTION_NAME_SELLER = 3;
    private static final int AUCTION_NAME_WINNER = 4;
    private static final int AUCTION_PRICE = 5;
    private static final int AUCTION_NuMBER_AUCTION = 6;
    private static final int AUCTION_BIDS = 7;

    public void loadAuctionCSV(String fileName) {
        Database database = Database.getInstance();
        BufferedReader fileReader = null;
        String line="";
        try {
            fileReader = new BufferedReader(new FileReader(fileName));
            while ((line = fileReader.readLine()) != null) {
                String[] data = line.split(COMMA_SEPARATOR);
                if (data.length > 0) {
                    Auction auction = new Auction(data[AUCTION_TITLE], data[AUCTION_DESCRIPTION], Database.getInstance().getCategoryByName(data[AUCTION_CATEGORY]), Database.getInstance().getAllUsersByNickname().get(data[AUCTION_NAME_SELLER]),Database.getInstance().getAllUsersByNickname().get(data[AUCTION_NAME_WINNER]),
                            new BigDecimal(data[AUCTION_PRICE]), Integer.parseInt(data[AUCTION_NuMBER_AUCTION]), Integer.parseInt(data[AUCTION_BIDS]));
                    database.addAuctionToAllAuctions(auction);
                    auction.getCategory().addAuction(auction);
                    database.getAllUsersByNickname().get(data[AUCTION_NAME_SELLER]).getMySellingList().add(auction);
                    if (auction.getBids() >=3) {
                        database.getAllUsersByNickname().get(data[AUCTION_NAME_WINNER]).getMyWonList().add(auction);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}