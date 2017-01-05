LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "master"
SRCREV_llvm = "b021632bce8d8e6485118a67c982f64e9fcf0839"
SRCREV_clang = "d407903caa09a280004a43f523734a6408b9fff1"
SRCREV_compiler-rt = "8378bf7059b49053de8982d34aba0948cff44cc6"
SRCREV_cxxabi = "3eacb7e06eb996c1015ee7e67c4f323f35c31208"
SRCREV_libcxx = "dceaa0e352e6db5defe1f63011c80550bc92037a"
SRCREV_libunwind = "1041783b9f43ede983fbca7e41aaf3300286fcd7"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
