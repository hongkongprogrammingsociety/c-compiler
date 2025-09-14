# C Compiler

Experimental c compiler

# test

```
mvn exec:java -Dexec.mainClass="org.hkprog.CCompiler" -Dexec.args="examples/test_function.c test_function.out"
docker run --rm -v $(pwd):/workspace -w /workspace ubuntu:latest bash -c "
apt update && apt install -y binutils && 
readelf -h test_function.out &&
readelf -S test_function.out &&
objdump -d test_function.out
"

docker run --rm -v $(pwd):/workspace -w /workspace ubuntu:latest bash -c "
apt update -q >/dev/null 2>&1 && apt install -y binutils >/dev/null 2>&1 && 
objdump -d test_function.out
"
```


# Author

Peter, \<peter@hkprog.org>, Chairman of [Hong Kong Programming Society](https://hkprog.org)



