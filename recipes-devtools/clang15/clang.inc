LLVM_GIT ?= "git://github.com/llvm"
LLVM_GIT_PROTOCOL ?= "https"

MAJOR_VER = "15"
MINOR_VER = "0"
PATCH_VER = "7"

LLVM_RELEASE = "${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}"
LLVM_DIR = "llvm${LLVM_RELEASE}"

SRCREV ?= "8dfdcc7b7bf66834a761bd8de445840ef68e4d1a"

PV = "${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}"
BRANCH = "release/15.x"

LLVMMD5SUM = "8a15a0759ef07f2682d2ba4b893c9afe"
CLANGMD5SUM = "ff42885ed2ab98f1ecb8c1fc41205343"
LLDMD5SUM = "ae7dc7c027b1fa89b5b013d391d3ee2b"
LLDBMD5SUM = "2e0d44968471fcde980034dbb826bea9"

LLVM_LIBDIR_SUFFIX="${@d.getVar('baselib').replace('lib', '')}"

# Counteract the instructions given in README.md
PREFERRED_PROVIDER_llvm = "clang15"
PREFERRED_PROVIDER_llvm-native = "clang15-native"
PREFERRED_PROVIDER_nativesdk-llvm = "nativesdk-clang15"

# Don't include these recipes in "bitbake world"
# as clang15 conflicts with clang.
EXCLUDE_FROM_WORLD = "1"

# set the default pigz thread
export PIGZ = "-p ${@oe.utils.cpu_count(at_least=2)}"

require common.inc
