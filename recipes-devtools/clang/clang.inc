LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "release_39"
SRCREV_llvm = "00d9b1b37f68373bb427369fee08814cda0692f7"
SRCREV_clang = "760f716545a51d39ec3f54cb4c96bf55c022099f"
SRCREV_compiler-rt = "c10168c3b09f714b9da56abcce1b48104c20e1f9"
SRCREV_cxxabi = "82bb4c89734b6157de4a2fc2b9228be6529337a8"
SRCREV_libcxx = "c2a735f1ff7a6680b10369e2b34324d1095a9732"
SRCREV_libunwind = "ce3e76f9fcb0c33b4683bc1a01bb66d1c87b2356"

LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
