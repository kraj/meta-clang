LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "release_40"
SRCREV_llvm = "506dff3e2adbd33a1b879380695b4ac0589d0c5a"
SRCREV_clang = "a2362c83ae458f5a85eaff44a72d4601fcf9a3f3"
SRCREV_compiler-rt = "1fdc27db84c9d0d9ae4ae60185629e8c43b4a11c"
SRCREV_cxxabi = "c4e6c8a10dea6f569fa99d56945ec1eba4be40dd"
SRCREV_libcxx = "0e8610273cf4b04dabc51c32fbad543abe00c665"
SRCREV_libunwind = "b8383c059f85f0d1249338a1775df967a1d0ff43"
SRCREV_lldb = "fcd2aac9f179b968a20cf0231c3386dcef8a6659"

LLVMMD5SUM = "e825e017edc35cfd58e26116e5251771"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"
