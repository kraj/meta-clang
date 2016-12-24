LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "master"
SRCREV_llvm = "356e45bf08b4994f3612ca29fe602aec10ec9555"
SRCREV_clang = "c311b06da6522cfdf2e28000e70ed4dfeb390b37"
SRCREV_compiler-rt = "054a09b3998670d99074bae79cf8b9d71711c1da"
SRCREV_cxxabi = "a3f4893c54ebd8662b1a8f079fa31beabeb54b90"
SRCREV_libcxx = "ab9ab94102311ce91c5837acdddbc4a9410f894e"
SRCREV_libunwind = "1041783b9f43ede983fbca7e41aaf3300286fcd7"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
