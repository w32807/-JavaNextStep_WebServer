package util;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public class HttpRequestUtils {
    /**
     * @param queryString은
     *            URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param 쿠키
     *            값은 name1=value1; name2=value2 형식임
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {    // 각각 String, Maps는 google의 guava 라이브러리에서 온다.
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator); // 각각의 tokens는 field1=value1의 형태가 됨.
        
        //1. Arrays.stream은 자바 8부터 사용할 수 있는 클래스이다. ( String 배열 tokens를 넣어 스트림으로 만든다)
        //2. stream의 인자를 1개씩 꺼내 t에 담고, map 함수로 t를 인자로 getKeyValue 메소드에 매핑한다. System.out::println의 형식은 System.out의 println을 실행. 즉 인자필요 없을 때!  
        //2-1. map을 실행하므로써 각각의 인자들이 다시 stream으로 만들어진다. (Pair객체들의 stream)
        //3. stream의 각각의 요소들을 filter로 걸러, 조건에 부합하는 데이터로만 다시 stream구성.
        //4. collect는 종료메소드. Collectors.to형태 로 하여 원하는 데이터 타입으로 반환가능
        //4-1. 여기서는 Map으로 반환하기 위하여 toMap을 이용 했으며, key , value로 구성됨
        //4-2. stream의 요소들을 꺼내(여기서는 pair객체), getKey,getValue 함수를 이용해 key와 value를 구성한 맵을 만듦.
        //5. 정리하면, stream으로 유효성 검사들을 한번에 처리 후 데이터를 Map으로 만들어줌.
        
        //Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).forEach(System.out::println);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) { //keyValue가 field1=value1 이므로, 길이가 2가 아니면 값을 입력받지 않거나 오류라는 이야기
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    public static class Pair {  //inner class
        String key;
        String value;

        Pair(String key, String value) {
            this.key = key.trim();
            this.value = value.trim();
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) { // 기본적으로 각각 new 되는 객체들은 다른 객체들이지만, 객체 안의 값이 같으면 같은 객체로 인식하게 하기위해 equals를 오버라이드하여 재정의한다.
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pair other = (Pair) obj;
            if (key == null) {
                if (other.key != null)
                    return false;
            } else if (!key.equals(other.key))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
