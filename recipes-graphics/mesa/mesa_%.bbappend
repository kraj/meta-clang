export YOCTO_ALTERNATE_EXE_PATH = "${STAGING_LIBDIR}/llvm-config"

CLANG_GALLIUM_LLVM = "-Dllvm=true -Dshared-llvm=true -Ddraw-use-llvm=true,-Dllvm=false,clang clang-native elfutils"

PACKAGECONFIG[gallium-llvm] := "${@[d.getVarFlag('PACKAGECONFIG', 'gallium-llvm'), '${CLANG_GALLIUM_LLVM}'][d.getVar('TOOLCHAIN') == 'clang']}"
