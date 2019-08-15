package gameover.fwk.libgdx

import java.nio.FloatBuffer
import java.nio.Buffer
import java.nio.IntBuffer

import com.badlogic.gdx.graphics.GL20

/**
 * Fake GL for test.
 */
class FakeGL20 : GL20 {
    override fun glUniform3i(location: Int, x: Int, y: Int, z: Int) {}

    override fun glLineWidth(width: Float) {}

    override fun glDeleteShader(shader: Int) {}

    override fun glDetachShader(program: Int, shader: Int) {}

    override fun glVertexAttrib3f(indx: Int, x: Float, y: Float, z: Float) {}

    override fun glCompileShader(shader: Int) {}

    override fun glTexParameterfv(target: Int, pname: Int, params: FloatBuffer?) {}

    override fun glStencilFunc(func: Int, ref: Int, mask: Int) {}

    override fun glDeleteFramebuffer(framebuffer: Int) {}

    override fun glGenTexture(): Int {
        return 0
    }

    override fun glBindAttribLocation(program: Int, index: Int, name: String?) {}

    override fun glEnableVertexAttribArray(index: Int) {}

    override fun glReleaseShaderCompiler() {}

    override fun glUniform2f(location: Int, x: Float, y: Float) {}

    override fun glGetActiveAttrib(program: Int, index: Int, size: IntBuffer?, type: Buffer?): String {
        return ""
    }

    override fun glGenFramebuffer(): Int {
        return 0
    }

    override fun glUniformMatrix2fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer?) {}

    override fun glUniformMatrix2fv(location: Int, count: Int, transpose: Boolean, value: FloatArray?, offset: Int) {}

    override fun glUniform2fv(location: Int, count: Int, v: FloatBuffer?) {
    }

    override fun glUniform2fv(location: Int, count: Int, v: FloatArray?, offset: Int) {
    }

    override fun glUniform4iv(location: Int, count: Int, v: IntBuffer?) {
    }

    override fun glUniform4iv(location: Int, count: Int, v: IntArray?, offset: Int) {
    }

    override fun glColorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean) {
    }

    override fun glPolygonOffset(factor: Float, units: Float) {
    }

    override fun glViewport(x: Int, y: Int, width: Int, height: Int) {
    }

    override fun glGetProgramiv(program: Int, pname: Int, params: IntBuffer?) {
    }

    override fun glGetBooleanv(pname: Int, params: Buffer?) {
    }

    override fun glGetBufferParameteriv(target: Int, pname: Int, params: IntBuffer?) {
    }

    override fun glDeleteTexture(texture: Int) {
    }

    override fun glGetVertexAttribiv(index: Int, pname: Int, params: IntBuffer?) {
    }

    override fun glVertexAttrib4fv(indx: Int, values: FloatBuffer?) {
        
    }

    override fun glTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, type: Int, pixels: Buffer?) {
        
    }

    override fun glDeleteRenderbuffers(n: Int, renderbuffers: IntBuffer?) {
        
    }

    override fun glGetTexParameteriv(target: Int, pname: Int, params: IntBuffer?) {
        
    }

    override fun glGenTextures(n: Int, textures: IntBuffer?) {
        
    }

    override fun glStencilOpSeparate(face: Int, fail: Int, zfail: Int, zpass: Int) {
        
    }

    override fun glUniform2i(location: Int, x: Int, y: Int) {
        
    }

    override fun glCheckFramebufferStatus(target: Int): Int {
        return 0
    }

    override fun glDeleteTextures(n: Int, textures: IntBuffer?) {
        
    }

    override fun glBindRenderbuffer(target: Int, renderbuffer: Int) {
        
    }

    override fun glTexParameteriv(target: Int, pname: Int, params: IntBuffer?) {
        
    }

    override fun glVertexAttrib4f(indx: Int, x: Float, y: Float, z: Float, w: Float) {
        
    }

    override fun glDeleteBuffers(n: Int, buffers: IntBuffer?) {
        
    }

    override fun glGetProgramInfoLog(program: Int): String {
        return ""
    }

    override fun glIsRenderbuffer(renderbuffer: Int): Boolean {
        return false
    }

    override fun glFrontFace(mode: Int) {
        
    }

    override fun glUniform1iv(location: Int, count: Int, v: IntBuffer?) {
        
    }

    override fun glUniform1iv(location: Int, count: Int, v: IntArray?, offset: Int) {
        
    }

    override fun glClearDepthf(depth: Float) {
        
    }

    override fun glBindTexture(target: Int, texture: Int) {
        
    }

    override fun glGetUniformLocation(program: Int, name: String?): Int {
        return 0
    }

    override fun glPixelStorei(pname: Int, param: Int) {
        
    }

    override fun glHint(target: Int, mode: Int) {
        
    }

    override fun glFramebufferRenderbuffer(target: Int, attachment: Int, renderbuffertarget: Int, renderbuffer: Int) {
        
    }

    override fun glUniform1f(location: Int, x: Float) {
        
    }

    override fun glDepthMask(flag: Boolean) {
        
    }

    override fun glBlendColor(red: Float, green: Float, blue: Float, alpha: Float) {
        
    }

    override fun glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer?) {
        
    }

    override fun glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: FloatArray?, offset: Int) {
        
    }

    override fun glBufferData(target: Int, size: Int, data: Buffer?, usage: Int) {
        
    }

    override fun glValidateProgram(program: Int) {
        
    }

    override fun glTexParameterf(target: Int, pname: Int, param: Float) {
        
    }

    override fun glIsFramebuffer(framebuffer: Int): Boolean {
        return false
    }

    override fun glDeleteBuffer(buffer: Int) {
        
    }

    override fun glShaderSource(shader: Int, string: String?) {
        
    }

    override fun glVertexAttrib2fv(indx: Int, values: FloatBuffer?) {
        
    }

    override fun glDeleteFramebuffers(n: Int, framebuffers: IntBuffer?) {
        
    }

    override fun glUniform4fv(location: Int, count: Int, v: FloatBuffer?) {
        
    }

    override fun glUniform4fv(location: Int, count: Int, v: FloatArray?, offset: Int) {
        
    }

    override fun glCompressedTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, imageSize: Int, data: Buffer?) {
        
    }

    override fun glGenerateMipmap(target: Int) {
        
    }

    override fun glDeleteProgram(program: Int) {
        
    }

    override fun glFramebufferTexture2D(target: Int, attachment: Int, textarget: Int, texture: Int, level: Int) {
        
    }

    override fun glGenRenderbuffer(): Int {
        return 0
    }

    override fun glAttachShader(program: Int, shader: Int) {
        
    }

    override fun glBindBuffer(target: Int, buffer: Int) {
        
    }

    override fun glShaderBinary(n: Int, shaders: IntBuffer?, binaryformat: Int, binary: Buffer?, length: Int) {
        
    }

    override fun glDisable(cap: Int) {
        
    }

    override fun glGetRenderbufferParameteriv(target: Int, pname: Int, params: IntBuffer?) {
        
    }

    override fun glGetShaderInfoLog(shader: Int): String {
        return ""
    }

    override fun glGetActiveUniform(program: Int, index: Int, size: IntBuffer?, type: Buffer?): String {
        return ""
    }

    override fun glClearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        
    }

    override fun glIsShader(shader: Int): Boolean {
        return false
    }

    override fun glUniform1i(location: Int, x: Int) {
        
    }

    override fun glBlendEquationSeparate(modeRGB: Int, modeAlpha: Int) {
        
    }

    override fun glScissor(x: Int, y: Int, width: Int, height: Int) {
        
    }

    override fun glCreateProgram(): Int {
        return 0
    }

    override fun glUniformMatrix3fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer?) {
        
    }

    override fun glUniformMatrix3fv(location: Int, count: Int, transpose: Boolean, value: FloatArray?, offset: Int) {
        
    }

    override fun glGetTexParameterfv(target: Int, pname: Int, params: FloatBuffer?) {
        
    }

    override fun glVertexAttrib1f(indx: Int, x: Float) {
        
    }

    override fun glUniform1fv(location: Int, count: Int, v: FloatBuffer?) {
        
    }

    override fun glUniform1fv(location: Int, count: Int, v: FloatArray?, offset: Int) {
        
    }

    override fun glUniform3iv(location: Int, count: Int, v: IntBuffer?) {
        
    }

    override fun glUniform3iv(location: Int, count: Int, v: IntArray?, offset: Int) {
        
    }

    override fun glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: Buffer?) {
        
    }

    override fun glVertexAttrib3fv(indx: Int, values: FloatBuffer?) {
        
    }

    override fun glBlendFunc(sfactor: Int, dfactor: Int) {
        
    }

    override fun glIsEnabled(cap: Int): Boolean {
        return false
    }

    override fun glGetAttribLocation(program: Int, name: String?): Int {
        return 0
    }

    override fun glDepthRangef(zNear: Float, zFar: Float) {
        
    }

    override fun glFlush() {
        
    }

    override fun glSampleCoverage(value: Float, invert: Boolean) {
        
    }

    override fun glCopyTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, x: Int, y: Int, width: Int, height: Int) {
        
    }

    override fun glGetShaderiv(shader: Int, pname: Int, params: IntBuffer?) {
        
    }

    override fun glGetUniformfv(program: Int, location: Int, params: FloatBuffer?) {
        
    }

    override fun glUniform4f(location: Int, x: Float, y: Float, z: Float, w: Float) {
        
    }

    override fun glClear(mask: Int) {
        
    }

    override fun glDepthFunc(func: Int) {
        
    }

    override fun glIsBuffer(buffer: Int): Boolean {
        return false
    }

    override fun glVertexAttribPointer(indx: Int, size: Int, type: Int, normalized: Boolean, stride: Int, ptr: Buffer?) {
        
    }

    override fun glVertexAttribPointer(indx: Int, size: Int, type: Int, normalized: Boolean, stride: Int, ptr: Int) {
        
    }

    override fun glStencilMaskSeparate(face: Int, mask: Int) {
        
    }

    override fun glDrawElements(mode: Int, count: Int, type: Int, indices: Buffer?) {
        
    }

    override fun glDrawElements(mode: Int, count: Int, type: Int, indices: Int) {
        
    }

    override fun glTexParameteri(target: Int, pname: Int, param: Int) {
        
    }

    override fun glUseProgram(program: Int) {
        
    }

    override fun glFinish() {
        
    }

    override fun glGetIntegerv(pname: Int, params: IntBuffer?) {
        
    }

    override fun glBlendEquation(mode: Int) {
        
    }

    override fun glUniform4i(location: Int, x: Int, y: Int, z: Int, w: Int) {
        
    }

    override fun glVertexAttrib1fv(indx: Int, values: FloatBuffer?) {
        
    }

    override fun glUniform3fv(location: Int, count: Int, v: FloatBuffer?) {
        
    }

    override fun glUniform3fv(location: Int, count: Int, v: FloatArray?, offset: Int) {
        
    }

    override fun glVertexAttrib2f(indx: Int, x: Float, y: Float) {
        
    }

    override fun glActiveTexture(texture: Int) {
        
    }

    override fun glCullFace(mode: Int) {
        
    }

    override fun glClearStencil(s: Int) {
        
    }

    override fun glGetFloatv(pname: Int, params: FloatBuffer?) {
        
    }

    override fun glDrawArrays(mode: Int, first: Int, count: Int) {
        
    }

    override fun glBindFramebuffer(target: Int, framebuffer: Int) {
        
    }

    override fun glGetError(): Int {
        return 0
    }

    override fun glBufferSubData(target: Int, offset: Int, size: Int, data: Buffer?) {
        
    }

    override fun glCopyTexImage2D(target: Int, level: Int, internalformat: Int, x: Int, y: Int, width: Int, height: Int, border: Int) {
        
    }

    override fun glIsProgram(program: Int): Boolean {
        return false
    }

    override fun glStencilOp(fail: Int, zfail: Int, zpass: Int) {
        
    }

    override fun glDisableVertexAttribArray(index: Int) {
        
    }

    override fun glGenBuffers(n: Int, buffers: IntBuffer?) {
        
    }

    override fun glGetAttachedShaders(program: Int, maxcount: Int, count: Buffer?, shaders: IntBuffer?) {
        
    }

    override fun glGenRenderbuffers(n: Int, renderbuffers: IntBuffer?) {
        
    }

    override fun glRenderbufferStorage(target: Int, internalformat: Int, width: Int, height: Int) {
        
    }

    override fun glUniform3f(location: Int, x: Float, y: Float, z: Float) {
        
    }

    override fun glReadPixels(x: Int, y: Int, width: Int, height: Int, format: Int, type: Int, pixels: Buffer?) {
        
    }

    override fun glStencilMask(mask: Int) {
        
    }

    override fun glBlendFuncSeparate(srcRGB: Int, dstRGB: Int, srcAlpha: Int, dstAlpha: Int) {
        
    }

    override fun glGetShaderPrecisionFormat(shadertype: Int, precisiontype: Int, range: IntBuffer?, precision: IntBuffer?) {
        
    }

    override fun glIsTexture(texture: Int): Boolean {
        return false
    }

    override fun glGetVertexAttribfv(index: Int, pname: Int, params: FloatBuffer?) {
        
    }

    override fun glGetVertexAttribPointerv(index: Int, pname: Int, pointer: Buffer?) {
        
    }

    override fun glCreateShader(type: Int): Int {
        return 0
    }

    override fun glStencilFuncSeparate(face: Int, func: Int, ref: Int, mask: Int) {
        
    }

    override fun glGetString(name: Int): String {
        return ""
    }

    override fun glCompressedTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, imageSize: Int, data: Buffer?) {
        
    }

    override fun glUniform2iv(location: Int, count: Int, v: IntBuffer?) {
        
    }

    override fun glUniform2iv(location: Int, count: Int, v: IntArray?, offset: Int) {
        
    }

    override fun glGenBuffer(): Int {
        return 0
    }

    override fun glEnable(cap: Int) {
        
    }

    override fun glGetUniformiv(program: Int, location: Int, params: IntBuffer?) {
        
    }

    override fun glGetFramebufferAttachmentParameteriv(target: Int, attachment: Int, pname: Int, params: IntBuffer?) {
        
    }

    override fun glDeleteRenderbuffer(renderbuffer: Int) {
        
    }

    override fun glGenFramebuffers(n: Int, framebuffers: IntBuffer?) {
        
    }

    override fun glLinkProgram(program: Int) {
        
    }

}