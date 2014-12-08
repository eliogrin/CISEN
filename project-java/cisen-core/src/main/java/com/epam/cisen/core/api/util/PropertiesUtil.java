package com.epam.cisen.core.api.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PropertiesUtil {

    private PropertiesUtil() {
    }

    public static boolean toBoolean(Object propValue, boolean defaultValue) {
        propValue = toObject(propValue);
        if (propValue instanceof Boolean)
            return (Boolean) propValue;
        if (propValue != null)
            return Boolean.valueOf(String.valueOf(propValue));
        else
            return defaultValue;
    }

    public static String toString(Object propValue, String defaultValue) {
        propValue = toObject(propValue);
        return propValue == null ? defaultValue : propValue.toString();
    }

    public static long toLong(Object propValue, long defaultValue) {
        propValue = toObject(propValue);
        if (propValue instanceof Long) {
            return (Long) propValue;
        }
        if (propValue == null) {
            throw new IllegalArgumentException("The property is not Long");
        } else {
            try {
                return Long.valueOf(String.valueOf(propValue));
            } catch (NumberFormatException ex) {
                return defaultValue;
            }
        }
    }

    public static int toInteger(Object propValue, int defaultValue) {
        propValue = toObject(propValue);
        if (propValue instanceof Integer) {
            return (Integer) propValue;
        }

        if (propValue == null) {
            throw new IllegalArgumentException("The property is not Integer");
        } else {
            try {
                return Integer.valueOf(String.valueOf(propValue));
            } catch (NumberFormatException ex) {
                return defaultValue;
            }
        }
    }

    public static double toDouble(Object propValue, double defaultValue) {
        propValue = toObject(propValue);
        if (propValue instanceof Double) {
            return (Double) propValue;
        }
        if (propValue == null) {
            throw new IllegalArgumentException("The property is not Double");
        } else {
            try {
                return Double.valueOf(String.valueOf(propValue));
            } catch (NumberFormatException ex) {
                return defaultValue;
            }
        }
    }

    public static Object toObject(Object propValue) {
        if (propValue == null) {
            return null;
        }

        if (propValue.getClass().isArray()) {
            Object prop[] = (Object[]) propValue;
            return prop.length <= 0 ? null : prop[0];
        }
        if (propValue instanceof Collection) {
            Collection prop = (Collection) propValue;
            return prop.isEmpty() ? null : prop.iterator().next();
        } else {
            return propValue;
        }
    }

    public static String[] toStringArray(Object propValue) {
        return toStringArray(propValue, null);
    }

    public static String[] toStringArray(Object propValue, String defaultArray[]) {
        if (propValue == null) {
            return defaultArray;
        }

        if (propValue instanceof String) {
            return (new String[] { (String) propValue });
        }

        if (propValue instanceof String[]) {
            return (String[]) propValue;
        }

        if (propValue.getClass().isArray()) {
            Object[] valueArray = (Object[]) propValue;
            List values = new ArrayList(valueArray.length);

            for (Object value : valueArray) {
                if (value != null)
                    values.add(value.toString());
            }

            return (String[]) values.toArray(new String[values.size()]);
        }

        if (propValue instanceof Collection) {
            Collection valueCollection = (Collection) propValue;
            List valueList = new ArrayList(valueCollection.size());
            Iterator i$ = valueCollection.iterator();
            do {
                if (!i$.hasNext()) {
                    break;
                }
                Object value = i$.next();
                if (value != null) {
                    valueList.add(value.toString());
                }
            } while (true);

            return (String[]) valueList.toArray(new String[valueList.size()]);
        } else {
            return defaultArray;
        }
    }

}
