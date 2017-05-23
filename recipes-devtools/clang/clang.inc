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
SRCREV_llvm = "ddfddd8ea50eb54b6d3a87c2668c23dce6b6f5ae"
SRCREV_clang = "f35b1d4090ccbfc38d1758d22eb359bd492ed7a7"
SRCREV_compiler-rt = "06f1c090cb4e27ea82320bfa7af1e3cfb681edeb"
SRCREV_cxxabi = "c515867bc14c433febcc574baedd081c078124d1"
SRCREV_libcxx = "64182a5877865cde2538c6038f98e3df33c93a03"
SRCREV_libunwind = "86219d8c6a73f95e694b4e1594e1a8a0a33613b6"
SRCREV_lld = "2658112fcdc127036cbef4d54c0e72ab2fd12de2"
SRCREV_lldb = "cf6c5b3386fa5b0667f1abe53bff8a6d4193afad"

LLVMMD5SUM = "e825e017edc35cfd58e26116e5251771"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
LLDMD5SUM = "c7343e1ccb3d65a14eba72ce1d548cb4"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"
