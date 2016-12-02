LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "master"
SRCREV_llvm = "7ac6b09c0fc5312f928b849b7763d4cbfad0c993"
SRCREV_clang = "ca715740dceadf31185d75d7ca7e91d135d73a22"
SRCREV_compiler-rt = "470fe8088d636d887f9a0957ca61ece1db5699bb"
SRCREV_cxxabi = "f1636207ad8e142a11f80860ea27ea92fcc8c776"
SRCREV_libcxx = "63f6af49d50207a69dc8f9e45729fdd3652acf24"
SRCREV_libunwind = "1041783b9f43ede983fbca7e41aaf3300286fcd7"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
