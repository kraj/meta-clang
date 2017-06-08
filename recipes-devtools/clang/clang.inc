LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#
MAJOR_VER="5"
MINOR_VER="0"
PATCH_VER="0"
PV = "${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}"
BRANCH = "master"
SRCREV_llvm = "5db707b0ff5ce707a78082219658a999b6b6eaa3"
SRCREV_clang = "5cfaf047c277163a75b7bab2fd58c812e7a13424"
SRCREV_compiler-rt = "4906f471eeabb1ffd1edb2c6cef5b5a5d790ddd7"
SRCREV_cxxabi = "35b048cf8b3be1a4171f26ca87504bdb10a9f2e9"
SRCREV_libcxx = "1cf5591af9f5f7d8a97ae069bc736c1250a35a33"
SRCREV_libunwind = "86219d8c6a73f95e694b4e1594e1a8a0a33613b6"
SRCREV_lld = "3372b8f0e7438bb3486abde6aa0c41ac17ba6688"
SRCREV_lldb = "50a43fd42566d6ab17f3c84ec1116a8bfd9c58f4"

LLVMMD5SUM = "e825e017edc35cfd58e26116e5251771"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
LLDMD5SUM = "c7343e1ccb3d65a14eba72ce1d548cb4"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"
