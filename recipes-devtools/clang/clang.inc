LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "master"
SRCREV_llvm = "0bada482941c96ff7103e7cf8c0cc43970613d85"
SRCREV_clang = "e2a2ad26076d3c3ad6359147ab5db78c0dc45500"
SRCREV_compiler-rt = "a8382f98913b040c4e79c6f1c24f546e5a86b0c9"
SRCREV_cxxabi = "2ecedcc0a1605ebeaa0afe3ab6fabe5dc8d5a2f8"
SRCREV_libcxx = "d3584d51a217251551330e7bdcc25a4be657c36f"
SRCREV_libunwind = "1041783b9f43ede983fbca7e41aaf3300286fcd7"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
