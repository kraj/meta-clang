LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "https"

#
# Uncomment below to enable master version of clang/llvm
#
MAJOR_VER = "7"
MINOR_VER = "0"
PATCH_VER = "0"

PV = "${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}"
BRANCH = "release_${MAJOR_VER}${MINOR_VER}"

SRCREV_llvm = "65ce2e56889af84e8be8e311f484a4dfe4b62d7a"
SRCREV_clang = "0513b409d5e34b2d2a28ae21b6d620cc52de0e57"
SRCREV_compiler-rt = "a4cbb02bca3b952117e9ccfbad8a485857f25935"
SRCREV_cxxabi = "809ebefc12d1cf3653a5459e7eb1cb13bb93fc39"
SRCREV_libcxx = "dffe9e0f1dde084f2aab8010345aeb1b7c8f7d4c"
SRCREV_libunwind = "d1dee9d525dac7d7146b6e197bc330a7bd963075"
SRCREV_lld = "e36f4d43a5525a63c4527324229d3fc4d2dc8a0f"
SRCREV_lldb = "434b6ab1b240bfdb60ba8ca645d3658bc7b27d81"
SRCREV_openmp = "366ce74b85790ed41f94fba7f17a0911bde83500"

LLVMMD5SUM = "c520ed40e11887bb1d24d86f7f5b1f05"
CLANGMD5SUM = "444af0e124949f07f791f12c928e5994"
LLDMD5SUM = "f4941ace8ddb3d6cf177fff94966319a"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"

COMPATIBLE_HOST_riscv64 = "(null)"
