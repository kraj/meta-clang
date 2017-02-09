LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "release_40"
SRCREV_llvm = "6af4d1ac2acfc8c22db959f2b7b11d68688b2425"
SRCREV_clang = "ee02028a97bfbce4d63787dd05366fafdda445c5"
SRCREV_compiler-rt = "855d4c102556800c64315ebf0f7e6339e58b6ccd"
SRCREV_cxxabi = "c4e6c8a10dea6f569fa99d56945ec1eba4be40dd"
SRCREV_libcxx = "b591390f4926d4a353c06af432a16fe7fb999570"
SRCREV_libunwind = "b8383c059f85f0d1249338a1775df967a1d0ff43"

LLVMMD5SUM = "e825e017edc35cfd58e26116e5251771"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
