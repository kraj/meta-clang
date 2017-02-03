LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "release_40"
SRCREV_llvm = "4c02744674eefe1d06247de20e68ab00ab75c061"
SRCREV_clang = "a6a1d3f3aa6c1d0018d84c3d6c26a552075982ab"
SRCREV_compiler-rt = "855d4c102556800c64315ebf0f7e6339e58b6ccd"
SRCREV_cxxabi = "c980f401c96324d8d5210fbbf9ba64154a740e7b"
SRCREV_libcxx = "ae248b3de76f1f25b9aa925743877b763e17e7b6"
SRCREV_libunwind = "9f88e323c3d0926f52ab3d4a5379eefd92ff28c2"

LLVMMD5SUM = "e825e017edc35cfd58e26116e5251771"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
