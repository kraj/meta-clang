LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#
MAJOR_VER = "6"
MINOR_VER = "0"
PATCH_VER = "1"

PV = "${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}"
BRANCH = "release_60"

SRCREV_llvm = "f1b37feef3d5f09dadf6a46fdb11fa7e4218cf6c"
SRCREV_clang = "0e746072ed897a85b4f533ab050b9f506941a097"
SRCREV_compiler-rt = "9d61c78bced84866cc886f1f1111c8e51c1d52d5"
SRCREV_cxxabi = "9245d481eb3e890f708ff2d7dadf2a10c04748ba"
SRCREV_libcxx = "48a89ecb9fd90f403ad8a7b061ff1bb18f34e132"
SRCREV_libunwind = "68c1bfc35687033aad28657404b80b1d558a4804"
SRCREV_lld = "bd6eb65ebaed5ee94d35d8073c417ec3c26f0b8a"
SRCREV_lldb = "b6df24ff1b258b18041161b8f32ac316a3b5d8d9"
SRCREV_openmp = "d5aa29cb3bcf51289d326b4e565613db8aff65ef"

LLVMMD5SUM = "e825e017edc35cfd58e26116e5251771"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
LLDMD5SUM = "c7343e1ccb3d65a14eba72ce1d548cb4"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"
