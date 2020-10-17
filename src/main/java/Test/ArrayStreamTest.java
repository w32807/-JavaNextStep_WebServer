package Test;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.Test;

public class ArrayStreamTest {
    
    @Test
    public void CreateStream() {
        // https://futurecreator.github.io/2018/08/26/java-8-streams/ 참조!!!
        //1. 배열을 매개변수로 생성
        String[] arr = new String[]{"a", "b", "c"};
        Stream<String> stream = Arrays.stream(arr);
        Stream<String> streamOfArrayPart = Arrays.stream(arr, 0, 2); // 이 때 매개변수는 arr배열의 0번부터 2-1번까지 stream으로 만들라는 의미 
        
        stream.forEach(System.out::println); // 문자열 배열을 인자로 stream을 만들어 출력
        System.out.println();
        streamOfArrayPart.forEach(System.out::println);
        System.out.println();
        
        //2. 비어있는 스트림 생성
        Stream.empty();
        
        //3. builder() 이용
        Stream<String> builderStream = Stream.<String>builder().add("Eric").add("Elena").add("Java").build(); // [Eric, Elena, Java]
        builderStream.forEach(System.out::println);
        System.out.println();
        
        //4. iterate() 이용
        Stream<Integer> iteratedStream = Stream.iterate(30, n -> n + 2).limit(5); // [30, 32, 34, 36, 38]
        iteratedStream.forEach(System.out::println);
        System.out.println();
        
        //5. generate() 이용 Supplier<T>에 해당하는 람다는, 매개변수는 없고 리턴값만 있는 함수형 인터페이스.
        Stream<String> generatedStream = Stream.generate(() -> "gen").limit(5); // [el, el, el, el, el]
        generatedStream .forEach(System.out::println);
        System.out.println();
    }
    
    

}
