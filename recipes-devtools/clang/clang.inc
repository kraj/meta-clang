LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "release_40"
SRCREV_llvm = "90a40679330e467b9836248b8619c33f261c9323"
SRCREV_clang = "559aa046fe3260d8640791f2249d7b0d458b5700"
SRCREV_compiler-rt = "1fdc27db84c9d0d9ae4ae60185629e8c43b4a11c"
SRCREV_cxxabi = "c4e6c8a10dea6f569fa99d56945ec1eba4be40dd"
SRCREV_libcxx = "af3664fd0469f18a9a0e30482f2f2c4445995a0c"
SRCREV_libunwind = "b8383c059f85f0d1249338a1775df967a1d0ff43"
SRCREV_lldb = "fcd2aac9f179b968a20cf0231c3386dcef8a6659"

LLVMMD5SUM = "e825e017edc35cfd58e26116e5251771"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"
