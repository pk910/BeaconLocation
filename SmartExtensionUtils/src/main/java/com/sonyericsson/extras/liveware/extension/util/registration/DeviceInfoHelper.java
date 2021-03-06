/*
Copyright (c) 2011, Sony Ericsson Mobile Communications AB

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

 * Neither the name of the Sony Ericsson Mobile Communications AB nor the names
  of its contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.sonyericsson.extras.liveware.extension.util.registration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;

import com.sonyericsson.extras.liveware.aef.notification.Notification;
import com.sonyericsson.extras.liveware.aef.registration.Registration;
import com.sonyericsson.extras.liveware.extension.util.Dbg;
import com.sonyericsson.extras.liveware.extension.util.R;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensor;

/**
 * This class contains helper functions for various devices
 */
public class DeviceInfoHelper {
    private static final int SMARTWATCH_2_API_LEVEL = 2;

    /**
     * The Control API level required for Smart Eyeglass
     */
    public static final int SMARTEYEGLASS_MIN_CONTROL_API_LEVEL = 4;

    /**
     * Checks host app API level and screen size to check if SmartWatch 2 is
     * supported.
     *
     * @param context The context.
     * @return true if SmartWatch is supported.
     */
    public static boolean isSmartWatch2ApiAndScreenDetected(Context context,
            String hostAppPackageName) {
        HostApplicationInfo hostApp = getHostApp(context, hostAppPackageName);
        if (hostApp == null) {
            Dbg.d("Host app was null, returning");
            return false;
        }
        // Get screen dimensions, unscaled
        final int controlSWWidth = getSmartWatch2Width(context);
        final int controlSWHeight = getSmartWatch2Height(context);

        if (hostApp.getControlApiVersion() >= SMARTWATCH_2_API_LEVEL) {
            for (DeviceInfo device : hostApp.getDevices()) {
                for (DisplayInfo display : device.getDisplays()) {
                    if (display.sizeEquals(controlSWWidth, controlSWHeight)) {
                        return true;
                    }
                }
            }
        } else {
            Dbg.d("Host had control API version: " + hostApp.getControlApiVersion() + ", returning");
        }
        return false;
    }

    /**
     * Check if SmartEyeglass screen size is supported.
     *
     * @param context The context.
     * @return true if SmartEyeglass display is supported.
     */
    public static boolean isSmartEyeglassScreenSupported(Context context,
            String hostAppPackageName) {
        HostApplicationInfo hostApp = getHostApp(context, hostAppPackageName);
        if (hostApp == null) {
            Dbg.d("Host app was null, returning");
            return false;
        }
        // Get screen dimensions, unscaled
        final int controlSWWidth = getSmartEyeglassWidth(context);
        final int controlSWHeight = getSmartEyeglassHeight(context);

        for (DeviceInfo device : hostApp.getDevices()) {
            for (DisplayInfo display : device.getDisplays()) {
                if (display.sizeEquals(controlSWWidth, controlSWHeight)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if host application supports a specific sensor.
     *
     * @param context The context.
     * @param hostAppPackageName The package name of the host application
     * @param sensorType The sensor type
     * @return true if the host application supports the sensor
     */
    public static boolean isSensorSupported(Context context, String hostAppPackageName,
            String sensorType) {
        HostApplicationInfo hostApp = getHostApp(context, hostAppPackageName);
        if (hostApp == null) {
            Dbg.d("Host app was null, bailing.");
        }
        else if (hostApp.getSensorApiVersion() > 0) {
            for (DeviceInfo device : hostApp.getDevices()) {
                for (AccessorySensor sensor : device.getSensors()) {
                    if (TextUtils.equals(sensor.getType().getName(), sensorType)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Gets the first connected host app
     *
     * @param hostAppPackageName The package name of the host application.
     * @return host app or null if no host app found
     */
    private static HostApplicationInfo getHostApp(Context context, String hostAppPackageName) {
        return RegistrationAdapter.getHostApplication(context, hostAppPackageName);
    }

    /**
     * Get SmartWatch 2 screen width.
     *
     * @param context The context.
     * @return width.
     */
    private static int getSmartWatch2Width(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null.");
        }
        return context.getResources().getDimensionPixelSize(R.dimen.smart_watch_2_control_width);
    }

    /**
     * Get SmartWatch 2 screen height.
     *
     * @param context The context.
     * @return height.
     */
    private static int getSmartWatch2Height(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null.");
        }
        return context.getResources().getDimensionPixelSize(R.dimen.smart_watch_2_control_height);
    }
    /**
     * Get SmartEyeglass screen width.
     *
     * @param context The context.
     * @return width.
     */
    public static int getSmartEyeglassWidth(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null.");
        }
        return context.getResources().getDimensionPixelSize(R.dimen.smarteyeglass_control_width);
    }

    /**
     * Get SmartEyeglass screen height.
     *
     * @param context The context.
     * @return height.
     */
    public static int getSmartEyeglassHeight(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null.");
        }
        return context.getResources().getDimensionPixelSize(R.dimen.smarteyeglass_control_height);
    }
    /**
     * Array of API keys that are in API 2 but not 1.
     */
    private static String[] API_2_KEYS = {
            Notification.SourceColumns.ACTION_ICON_1,
            Notification.SourceColumns.ACTION_ICON_2,
            Notification.SourceColumns.ACTION_ICON_3,
            Notification.SourceColumns.COLOR,
            Notification.SourceColumns.SUPPORTS_REFRESH,

            Notification.SourceEventColumns.ACTION_ICON_1,
            Notification.SourceEventColumns.ACTION_ICON_2,
            Notification.SourceEventColumns.ACTION_ICON_3,
            Notification.SourceEventColumns.COLOR,

            Registration.ApiRegistrationColumns.CONTROL_BACK_INTERCEPT,
            Registration.ApiRegistrationColumns.LOW_POWER_SUPPORT,

            Registration.ExtensionColumns.EXTENSION_48PX_ICON_URI,
            Registration.ExtensionColumns.LAUNCH_MODE
    };

    private static int smartConnectLevel = -1;

    /**
     * Iterates through ContentValues and removes values that are not available
     * for the given SmartConnect version, regardless of which table the value
     * is in. Current implementation only supports API levels 1 and 2. May not
     * be complete.
     *
     * @param context The context.
     * @param apiLevel The version of the current API level, contentvalues not
     *            supported in this API level will be removed
     * @param values the ContentValues to be scanned for unsafe values
     * @return removeValues
     */
    static int removeUnsafeValues(Context context, int apiLevel, ContentValues values) {
        if (values == null) {
            throw new IllegalArgumentException("values is null.");
        }
        int removedValues = 0;

        if (apiLevel < 2) {
            for (String key : API_2_KEYS) {
                if (values.containsKey(key)) {
                    values.remove(key);
                    Dbg.d("Removing " + key + " key from contentvalues");
                    removedValues++;
                }
            }
        }
        Dbg.e("Removed " + removedValues + " values from contentvalues");
        return removedValues;
    }

    /**
     * Removes ContentValues that don't match the current SmartConnect version.
     * NOTE: This method can not be called before extension is registered
     *
     * @see DeviceInfoHelper#removeUnsafeValues(Context, int, ContentValues)
     * @param context The Context.
     * @param values The ContentValues to be scanned for unsafe values.
     * @return removeValues
     */
    public static int removeUnsafeValues(Context context, ContentValues values) {

        if (smartConnectLevel == -1) {
            smartConnectLevel = getSmartConnectVersion(context);
        }
        return removeUnsafeValues(context, smartConnectLevel, values);
    }

    /**
     * Gets the SmartConnect version. There's no explicit versioning in
     * SmartConnect, and generally it's not required. This method infers the
     * version based on database columns.
     *
     * @param context The context.
     * @return The inferred SmartConnect version
     */
    private static int getSmartConnectVersion(final Context context) {
        Cursor cursor = null;
        if (context == null) {
            throw new IllegalArgumentException("context is null.");
        }
        try {
            cursor = context.
                    getContentResolver().query(Registration.Extension.URI,
                            null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {

                // The EXTENSION_48PX_ICON_URI column was added in HostApp api
                // level 2. Since
                // this column exists we know we can safely add other column
                // from this API level
                int lowPowerColumn = cursor.getColumnIndex(
                        Registration.ExtensionColumns.EXTENSION_48PX_ICON_URI);
                if (lowPowerColumn != -1) {
                    Dbg.d("SmartConnect version 2 or higher detected");
                    return 2;
                } else {
                    Dbg.d("SmartConnect version 1 detected");
                    return 1;
                }
            }
        } catch (SQLException e) {
            if (Dbg.DEBUG) {
                Dbg.w("Failed to query host application", e);
            }
        } catch (SecurityException e) {
            if (Dbg.DEBUG) {
                Dbg.w("Failed to query host application", e);
            }
        } catch (IllegalArgumentException e) {
            if (Dbg.DEBUG) {
                Dbg.w("Failed to query host application", e);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 1;
    }
}
