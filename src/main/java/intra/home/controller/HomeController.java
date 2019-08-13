package intra.home.controller;

import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
public class HomeController {

    private final BigDecimal BYTES_IN_GB = new BigDecimal(1_073_741_824);

    @GetMapping("/get/available/disk/space")
    public double getAvailableDiskSpace() {
        File file = new File("/");
        long usableSpace = file.getUsableSpace();
        return new BigDecimal(usableSpace)
                .divide(BYTES_IN_GB, 2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @GetMapping("/get/total/disk/space")
    public double getTotalDiskSpace() {
        File file = new File("/");
        long totalSpace = file.getTotalSpace();
        return new BigDecimal(totalSpace)
                .divide(BYTES_IN_GB, 2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
