LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#
BASEPV = "4.0.0"
PV = "${BASEPV}"
BRANCH = "release_40"
SRCREV_llvm = "4423e351176a92975739dd4ea43c2ff5877236ae"
SRCREV_clang = "559aa046fe3260d8640791f2249d7b0d458b5700"
SRCREV_compiler-rt = "1fdc27db84c9d0d9ae4ae60185629e8c43b4a11c"
SRCREV_cxxabi = "c4e6c8a10dea6f569fa99d56945ec1eba4be40dd"
SRCREV_libcxx = "af3664fd0469f18a9a0e30482f2f2c4445995a0c"
SRCREV_libunwind = "e1a24d4951a60ea51a7bd2505d6cc16c0bcb2c61"
SRCREV_lldb = "fcd2aac9f179b968a20cf0231c3386dcef8a6659"

LLVMMD5SUM = "e825e017edc35cfd58e26116e5251771"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"
