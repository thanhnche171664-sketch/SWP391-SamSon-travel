package util;

import dao.TourDAO;
import dao.TourPackageDAO;
import dao.TourScheduleDAO;
import entity.Tour;
import entity.TourPackage;
import entity.TourSchedule;

public class PricingService {

    private final TourDAO tourDAO = new TourDAO();
    private final TourScheduleDAO scheduleDAO = new TourScheduleDAO();
    private final TourPackageDAO packageDAO = new TourPackageDAO();

    public static class PriceBreakdown {
        public double base;
        public double scheduleAdj;
        public double packagePrice;
        public double subtotal;
        public double discount;
        public double tax;
        public double total;
    }

    public PriceBreakdown compute(int tourId, int scheduleId, Integer packageId, int guestCount) {
        Tour tour = tourDAO.getTourById(tourId);
        TourSchedule schedule = scheduleDAO.getScheduleById(scheduleId);
        TourPackage pkg = (packageId == null ? null : packageDAO.getPackageById(packageId));

        double base = tour.getBasePrice() * guestCount;
        double scheduleAdj = schedule.getPriceAdjustment() * guestCount;
        double pkgPrice = (pkg == null ? 0 : pkg.getPrice() * guestCount);

        double subtotal = base + scheduleAdj + pkgPrice;
        double discount = 0; // placeholder for future discount logic
        double tax = 0; // placeholder if VAT applies
        double total = subtotal - discount + tax;

        PriceBreakdown res = new PriceBreakdown();
        res.base = base; res.scheduleAdj = scheduleAdj; res.packagePrice = pkgPrice;
        res.subtotal = subtotal; res.discount = discount; res.tax = tax; res.total = total;
        return res;
    }
}


