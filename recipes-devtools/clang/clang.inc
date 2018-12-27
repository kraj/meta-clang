LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-project"
LLVM_GIT_PROTOCOL ?= "https"

MAJOR_VER = "7"
MINOR_VER = "0"
PATCH_VER = "1"

SRCREV ?= "b8e7044dd2e6e7af9e58cef99b840bd88cfd2dec"

PV = "${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}"
BRANCH = "release_${MAJOR_VER}${MINOR_VER}"

LLVMMD5SUM = "c520ed40e11887bb1d24d86f7f5b1f05"
CLANGMD5SUM = "444af0e124949f07f791f12c928e5994"
LLDMD5SUM = "f4941ace8ddb3d6cf177fff94966319a"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"

# risc-v comes with clang-8+
COMPATIBLE_HOST_riscv64 = "(null)"

require common.inc
