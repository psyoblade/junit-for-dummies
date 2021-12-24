package me.suhyuk.utils;

public class Bar {

    public String foo(StringUtils utils, String str) {
        return utils.upper(str);
    }

    public String bar(DummyUtils utils, String str) {
        return utils.lower(str);
    }

    public static void main(String[] args) {
        Bar bar = new Bar();
        StringUtils toUpper = new StringUtils() {
            @Override
            public String upper(String str) {
                return str.toUpperCase();
            }
        };
        DummyUtils toLower = new DummyUtils() {
            @Override
            public String lower(String str) {
                return str.toLowerCase();
            }

            @Override
            public String upper(String str) {
                return null;
            }
        };
        assert(bar.foo(toUpper, "park.suhyuk").equals("PARK.SUHYUK"));
        assert(bar.bar(toLower, "PARK.SUHYUK").equals("park.suhyuk"));
        assert(bar.foo(p -> p.toUpperCase(), "park.suhyuk").equals("PARK.SUHYUK"));
    }
}
