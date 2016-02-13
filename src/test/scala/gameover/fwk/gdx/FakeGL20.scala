package gameover.fwk.gdx

import java.nio.{FloatBuffer, Buffer, IntBuffer}

import com.badlogic.gdx.graphics.GL20

/**
 * Fake GL for test.
 */
class FakeGL20 extends GL20 {
  override def glUseProgram(program: Int){}

  override def glCreateProgram(): Int = 0

  override def glDisableVertexAttribArray(index: Int){}

  override def glGenerateMipmap(target: Int) {}

  override def glUniform1i(location: Int, x: Int) {}

  override def glGetAttachedShaders(program: Int, maxcount: Int, count: Buffer, shaders: IntBuffer){}

  override def glGenBuffer(): Int = 0

  override def glFramebufferRenderbuffer(target: Int, attachment: Int, renderbuffertarget: Int, renderbuffer: Int){}

  override def glUniform3fv(location: Int, count: Int, v: FloatBuffer){}

  override def glUniform3fv(location: Int, count: Int, v: Array[Float], offset: Int){}

  override def glGetUniformfv(program: Int, location: Int, params: FloatBuffer){}

  override def glIsBuffer(buffer: Int): Boolean = true

  override def glDepthFunc(func: Int){}

  override def glGetShaderPrecisionFormat(shadertype: Int, precisiontype: Int, range: IntBuffer, precision: IntBuffer){}

  override def glGetAttribLocation(program: Int, name: String): Int = 0

  override def glValidateProgram(program: Int){}

  override def glGetTexParameteriv(target: Int, pname: Int, params: IntBuffer){}

  override def glStencilFuncSeparate(face: Int, func: Int, ref: Int, mask: Int){}

  override def glClear(mask: Int){}

  override def glEnableVertexAttribArray(index: Int){}

  override def glVertexAttrib1f(indx: Int, x: Float){}

  override def glUniformMatrix3fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer){}

  override def glUniformMatrix3fv(location: Int, count: Int, transpose: Boolean, value: Array[Float], offset: Int){}

  override def glUniform3f(location: Int, x: Float, y: Float, z: Float){}

  override def glGetShaderInfoLog(shader: Int): String = ""

  override def glClearStencil(s: Int){}

  override def glBindTexture(target: Int, texture: Int){}

  override def glStencilMaskSeparate(face: Int, mask: Int){}

  override def glGenFramebuffers(n: Int, framebuffers: IntBuffer){}

  override def glCopyTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, x: Int, y: Int, width: Int, height: Int){}

  override def glUniform3i(location: Int, x: Int, y: Int, z: Int){}

  override def glVertexAttrib4f(indx: Int, x: Float, y: Float, z: Float, w: Float){}

  override def glIsRenderbuffer(renderbuffer: Int): Boolean = true

  override def glGetTexParameterfv(target: Int, pname: Int, params: FloatBuffer){}

  override def glHint(target: Int, mode: Int){}

  override def glScissor(x: Int, y: Int, width: Int, height: Int){}

  override def glDeleteTexture(texture: Int){}

  override def glEnable(cap: Int){}

  override def glCullFace(mode: Int){}

  override def glGenBuffers(n: Int, buffers: IntBuffer){}

  override def glIsTexture(texture: Int): Boolean = true

  override def glGetIntegerv(pname: Int, params: IntBuffer){}

  override def glVertexAttrib3fv(indx: Int, values: FloatBuffer){}

  override def glIsFramebuffer(framebuffer: Int): Boolean = true

  override def glDepthRangef(zNear: Float, zFar: Float){}

  override def glGetFramebufferAttachmentParameteriv(target: Int, attachment: Int, pname: Int, params: IntBuffer){}

  override def glFlush(){}

  override def glDeleteRenderbuffers(n: Int, renderbuffers: IntBuffer){}

  override def glDeleteTextures(n: Int, textures: IntBuffer){}

  override def glCompileShader(shader: Int){}

  override def glStencilOp(fail: Int, zfail: Int, zpass: Int){}

  override def glBindAttribLocation(program: Int, index: Int, name: String){}

  override def glUniform2iv(location: Int, count: Int, v: IntBuffer){}

  override def glUniform2iv(location: Int, count: Int, v: Array[Int], offset: Int){}

  override def glViewport(x: Int, y: Int, width: Int, height: Int){}

  override def glDeleteFramebuffers(n: Int, framebuffers: IntBuffer){}

  override def glPixelStorei(pname: Int, param: Int){}

  override def glBindFramebuffer(target: Int, framebuffer: Int){}

  override def glBlendFunc(sfactor: Int, dfactor: Int){}

  override def glGetBooleanv(pname: Int, params: Buffer){}

  override def glGetError(): Int = 0

  override def glFramebufferTexture2D(target: Int, attachment: Int, textarget: Int, texture: Int, level: Int){}

  override def glShaderBinary(n: Int, shaders: IntBuffer, binaryformat: Int, binary: Buffer, length: Int){}

  override def glTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, `type`: Int, pixels: Buffer){}

  override def glReleaseShaderCompiler(){}

  override def glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, `type`: Int, pixels: Buffer){}

  override def glUniform2fv(location: Int, count: Int, v: FloatBuffer){}

  override def glUniform2fv(location: Int, count: Int, v: Array[Float], offset: Int){}

  override def glGetString(name: Int): String = ""

  override def glActiveTexture(texture: Int){}

  override def glGenTextures(n: Int, textures: IntBuffer){}

  override def glBufferSubData(target: Int, offset: Int, size: Int, data: Buffer){}

  override def glDeleteBuffers(n: Int, buffers: IntBuffer){}

  override def glVertexAttrib3f(indx: Int, x: Float, y: Float, z: Float){}

  override def glClearDepthf(depth: Float){}

  override def glGenFramebuffer(): Int = 0

  override def glDeleteShader(shader: Int){}

  override def glUniformMatrix2fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer){}

  override def glUniformMatrix2fv(location: Int, count: Int, transpose: Boolean, value: Array[Float], offset: Int){}

  override def glBufferData(target: Int, size: Int, data: Buffer, usage: Int){}

  override def glBlendColor(red: Float, green: Float, blue: Float, alpha: Float){}

  override def glReadPixels(x: Int, y: Int, width: Int, height: Int, format: Int, `type`: Int, pixels: Buffer){}

  override def glTexParameteriv(target: Int, pname: Int, params: IntBuffer){}

  override def glUniform2i(location: Int, x: Int, y: Int){}

  override def glGetVertexAttribPointerv(index: Int, pname: Int, pointer: Buffer){}

  override def glUniform4iv(location: Int, count: Int, v: IntBuffer){}

  override def glUniform4iv(location: Int, count: Int, v: Array[Int], offset: Int){}

  override def glGetBufferParameteriv(target: Int, pname: Int, params: IntBuffer){}

  override def glStencilMask(mask: Int){}

  override def glDeleteBuffer(buffer: Int){}

  override def glDrawElements(mode: Int, count: Int, `type`: Int, indices: Buffer){}

  override def glDrawElements(mode: Int, count: Int, `type`: Int, indices: Int){}

  override def glDeleteProgram(program: Int){}

  override def glBindBuffer(target: Int, buffer: Int){}

  override def glVertexAttrib2fv(indx: Int, values: FloatBuffer){}

  override def glFinish(){}

  override def glDrawArrays(mode: Int, first: Int, count: Int){}

  override def glRenderbufferStorage(target: Int, internalformat: Int, width: Int, height: Int){}

  override def glBindRenderbuffer(target: Int, renderbuffer: Int){}

  override def glIsProgram(program: Int): Boolean = true

  override def glGetUniformLocation(program: Int, name: String): Int = 0

  override def glUniform2f(location: Int, x: Float, y: Float){}

  override def glTexParameterfv(target: Int, pname: Int, params: FloatBuffer){}

  override def glLinkProgram(program: Int){}

  override def glGetActiveAttrib(program: Int, index: Int, size: IntBuffer, `type`: Buffer): String = ""

  override def glCompressedTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, imageSize: Int, data: Buffer){}

  override def glBlendEquation(mode: Int){}

  override def glGenRenderbuffer(): Int = 0

  override def glGetProgramiv(program: Int, pname: Int, params: IntBuffer){}

  override def glUniform1iv(location: Int, count: Int, v: IntBuffer){}

  override def glUniform1iv(location: Int, count: Int, v: Array[Int], offset: Int){}

  override def glGetShaderiv(shader: Int, pname: Int, params: IntBuffer){}

  override def glUniform4fv(location: Int, count: Int, v: FloatBuffer){}

  override def glUniform4fv(location: Int, count: Int, v: Array[Float], offset: Int){}

  override def glAttachShader(program: Int, shader: Int){}

  override def glSampleCoverage(value: Float, invert: Boolean){}

  override def glColorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean){}

  override def glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: FloatBuffer){}

  override def glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: Array[Float], offset: Int){}

  override def glGetProgramInfoLog(program: Int): String = ""

  override def glStencilOpSeparate(face: Int, fail: Int, zfail: Int, zpass: Int){}

  override def glIsEnabled(cap: Int): Boolean = true

  override def glGenTexture(): Int = 0

  override def glGetVertexAttribiv(index: Int, pname: Int, params: IntBuffer){}

  override def glVertexAttrib1fv(indx: Int, values: FloatBuffer){}

  override def glLineWidth(width: Float){}

  override def glDisable(cap: Int){}

  override def glUniform1fv(location: Int, count: Int, v: FloatBuffer){}

  override def glUniform1fv(location: Int, count: Int, v: Array[Float], offset: Int){}

  override def glIsShader(shader: Int): Boolean = true

  override def glDeleteFramebuffer(framebuffer: Int){}

  override def glDetachShader(program: Int, shader: Int){}

  override def glCopyTexImage2D(target: Int, level: Int, internalformat: Int, x: Int, y: Int, width: Int, height: Int, border: Int){}

  override def glUniform4f(location: Int, x: Float, y: Float, z: Float, w: Float){}

  override def glGenRenderbuffers(n: Int, renderbuffers: IntBuffer){}

  override def glCompressedTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, imageSize: Int, data: Buffer){}

  override def glTexParameterf(target: Int, pname: Int, param: Float){}

  override def glVertexAttrib2f(indx: Int, x: Float, y: Float){}

  override def glBlendEquationSeparate(modeRGB: Int, modeAlpha: Int){}

  override def glUniform4i(location: Int, x: Int, y: Int, z: Int, w: Int){}

  override def glDeleteRenderbuffer(renderbuffer: Int){}

  override def glVertexAttrib4fv(indx: Int, values: FloatBuffer){}

  override def glClearColor(red: Float, green: Float, blue: Float, alpha: Float){}

  override def glGetRenderbufferParameteriv(target: Int, pname: Int, params: IntBuffer){}

  override def glCreateShader(`type`: Int): Int = 0

  override def glGetVertexAttribfv(index: Int, pname: Int, params: FloatBuffer){}

  override def glTexParameteri(target: Int, pname: Int, param: Int){}

  override def glPolygonOffset(factor: Float, units: Float){}

  override def glGetFloatv(pname: Int, params: FloatBuffer){}

  override def glFrontFace(mode: Int){}

  override def glStencilFunc(func: Int, ref: Int, mask: Int){}

  override def glShaderSource(shader: Int, string: String){}

  override def glUniform1f(location: Int, x: Float){}

  override def glVertexAttribPointer(indx: Int, size: Int, `type`: Int, normalized: Boolean, stride: Int, ptr: Buffer){}

  override def glVertexAttribPointer(indx: Int, size: Int, `type`: Int, normalized: Boolean, stride: Int, ptr: Int){}

  override def glCheckFramebufferStatus(target: Int): Int = 0

  override def glBlendFuncSeparate(srcRGB: Int, dstRGB: Int, srcAlpha: Int, dstAlpha: Int){}

  override def glGetUniformiv(program: Int, location: Int, params: IntBuffer){}

  override def glGetActiveUniform(program: Int, index: Int, size: IntBuffer, `type`: Buffer): String = ""

  override def glUniform3iv(location: Int, count: Int, v: IntBuffer){}

  override def glUniform3iv(location: Int, count: Int, v: Array[Int], offset: Int){}

  override def glDepthMask(flag: Boolean){}
}
