LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "master"
SRCREV_llvm = "aea637329e650a06c199f23f078cf0ad55489445"
SRCREV_clang = "8a6ea813424dabc71bf4514942e487bd0268a317"
SRCREV_compiler-rt = "4eb08d1dbd9bdfe505c146fcfbcdf94e9d26a173"
SRCREV_cxxabi = "2ecedcc0a1605ebeaa0afe3ab6fabe5dc8d5a2f8"
SRCREV_libcxx = "6f7f0b85300a16614c7899d7877ba86a6460996a"
SRCREV_libunwind = "1041783b9f43ede983fbca7e41aaf3300286fcd7"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
