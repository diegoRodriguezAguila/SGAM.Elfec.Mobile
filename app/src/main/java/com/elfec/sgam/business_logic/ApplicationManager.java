package com.elfec.sgam.business_logic;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

/**
 * ElfecApplication manager
 */
public class ApplicationManager {
    /**
     * Return whether the given PackgeInfo represents a system package or not.
     * User-installed packages (Market or otherwise) should not be denoted as
     * system packages.
     *
     * @param pkgInfo
     * @return true if it is a system package
     */
    public static boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}
