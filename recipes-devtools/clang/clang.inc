LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm"
LLVM_GIT_PROTOCOL ?= "https"

MAJOR_VER = "18"
MINOR_VER = "1"
PATCH_VER = "0"
# could be 'rcX' or 'git' or empty ( for release )
VER_SUFFIX = ""
SRCREV ?= "0d656f0f12d447ac0e197618a0d25495d66388d1"

PV = "${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}${VER_SUFFIX}"
BRANCH = "release/18.x"

LLVMMD5SUM = "8a15a0759ef07f2682d2ba4b893c9afe"
CLANGMD5SUM = "ff42885ed2ab98f1ecb8c1fc41205343"
LLDMD5SUM = "ae7dc7c027b1fa89b5b013d391d3ee2b"
LLDBMD5SUM = "2e0d44968471fcde980034dbb826bea9"

LLVM_LIBDIR_SUFFIX="${@d.getVar('baselib').replace('lib', '')}"

# set the default pigz thread
export PIGZ = "-p ${@oe.utils.cpu_count(at_least=2)}"

require common.inc
