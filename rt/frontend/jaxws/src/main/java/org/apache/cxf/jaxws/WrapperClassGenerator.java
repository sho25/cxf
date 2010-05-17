begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|GenericArrayType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|ParameterizedType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttachmentRef
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlList
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlMimeType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlNsForm
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|XmlJavaTypeAdapter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|ASMHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|PackageUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|JavaUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxb
operator|.
name|JAXBUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|support
operator|.
name|JaxWsServiceFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|factory
operator|.
name|ReflectionServiceFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|InterfaceInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|MessageInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|OperationInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|SchemaInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|ToolConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|AnnotationVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|ClassWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|FieldVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|Label
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|MethodVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|Opcodes
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|WrapperClassGenerator
extends|extends
name|ASMHelper
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|WrapperClassGenerator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|wrapperBeans
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|InterfaceInfo
name|interfaceInfo
decl_stmt|;
specifier|private
name|boolean
name|qualified
decl_stmt|;
specifier|private
name|JaxWsServiceFactoryBean
name|factory
decl_stmt|;
specifier|public
name|WrapperClassGenerator
parameter_list|(
name|JaxWsServiceFactoryBean
name|fact
parameter_list|,
name|InterfaceInfo
name|inf
parameter_list|,
name|boolean
name|q
parameter_list|)
block|{
name|factory
operator|=
name|fact
expr_stmt|;
name|interfaceInfo
operator|=
name|inf
expr_stmt|;
name|qualified
operator|=
name|q
expr_stmt|;
block|}
specifier|private
name|String
name|getPackageName
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|String
name|pkg
init|=
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|method
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|pkg
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|?
name|ToolConstants
operator|.
name|DEFAULT_PACKAGE_NAME
else|:
name|pkg
return|;
block|}
specifier|private
name|Annotation
index|[]
name|getMethodParameterAnnotations
parameter_list|(
specifier|final
name|MessagePartInfo
name|mpi
parameter_list|)
block|{
name|Annotation
index|[]
name|a
init|=
operator|(
name|Annotation
index|[]
operator|)
name|mpi
operator|.
name|getProperty
argument_list|(
name|ReflectionServiceFactoryBean
operator|.
name|PARAM_ANNOTATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|!=
literal|null
condition|)
block|{
return|return
name|a
return|;
block|}
name|Annotation
index|[]
index|[]
name|paramAnno
init|=
operator|(
name|Annotation
index|[]
index|[]
operator|)
name|mpi
operator|.
name|getProperty
argument_list|(
name|ReflectionServiceFactoryBean
operator|.
name|METHOD_PARAM_ANNOTATIONS
argument_list|)
decl_stmt|;
name|int
name|index
init|=
name|mpi
operator|.
name|getIndex
argument_list|()
decl_stmt|;
if|if
condition|(
name|paramAnno
operator|!=
literal|null
operator|&&
name|index
operator|<
name|paramAnno
operator|.
name|length
operator|&&
name|index
operator|>=
literal|0
condition|)
block|{
return|return
name|paramAnno
index|[
name|index
index|]
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|List
argument_list|<
name|Annotation
argument_list|>
name|getJaxbAnnos
parameter_list|(
name|MessagePartInfo
name|mpi
parameter_list|)
block|{
name|List
argument_list|<
name|Annotation
argument_list|>
name|list
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArrayList
argument_list|<
name|Annotation
argument_list|>
argument_list|()
decl_stmt|;
name|Annotation
index|[]
name|anns
init|=
name|getMethodParameterAnnotations
argument_list|(
name|mpi
argument_list|)
decl_stmt|;
if|if
condition|(
name|anns
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Annotation
name|anno
range|:
name|anns
control|)
block|{
if|if
condition|(
name|anno
operator|.
name|annotationType
argument_list|()
operator|==
name|XmlList
operator|.
name|class
operator|||
name|anno
operator|.
name|annotationType
argument_list|()
operator|==
name|XmlAttachmentRef
operator|.
name|class
operator|||
name|anno
operator|.
name|annotationType
argument_list|()
operator|==
name|XmlJavaTypeAdapter
operator|.
name|class
operator|||
name|anno
operator|.
name|annotationType
argument_list|()
operator|==
name|XmlMimeType
operator|.
name|class
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|anno
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|list
return|;
block|}
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|generate
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|createClassWriter
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|()
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
for|for
control|(
name|OperationInfo
name|opInfo
range|:
name|interfaceInfo
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|opInfo
operator|.
name|isUnwrappedCapable
argument_list|()
operator|&&
operator|(
name|opInfo
operator|.
name|getUnwrappedOperation
argument_list|()
operator|.
name|getProperty
argument_list|(
name|ReflectionServiceFactoryBean
operator|.
name|WRAPPERGEN_NEEDED
argument_list|)
operator|!=
literal|null
operator|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
name|opInfo
operator|.
name|getName
argument_list|()
operator|+
literal|"requires a wrapper bean but problems with"
operator|+
literal|" ASM has prevented creating one.  Operation may not work correctly."
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|wrapperBeans
return|;
block|}
for|for
control|(
name|OperationInfo
name|opInfo
range|:
name|interfaceInfo
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|opInfo
operator|.
name|isUnwrappedCapable
argument_list|()
condition|)
block|{
name|Method
name|method
init|=
operator|(
name|Method
operator|)
name|opInfo
operator|.
name|getProperty
argument_list|(
name|ReflectionServiceFactoryBean
operator|.
name|METHOD
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|MessagePartInfo
name|inf
init|=
name|opInfo
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|inf
operator|.
name|getTypeClass
argument_list|()
operator|==
literal|null
condition|)
block|{
name|MessageInfo
name|messageInfo
init|=
name|opInfo
operator|.
name|getUnwrappedOperation
argument_list|()
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|createWrapperClass
argument_list|(
name|inf
argument_list|,
name|messageInfo
argument_list|,
name|opInfo
argument_list|,
name|method
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|MessageInfo
name|messageInfo
init|=
name|opInfo
operator|.
name|getUnwrappedOperation
argument_list|()
operator|.
name|getOutput
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageInfo
operator|!=
literal|null
condition|)
block|{
name|inf
operator|=
name|opInfo
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|inf
operator|.
name|getTypeClass
argument_list|()
operator|==
literal|null
condition|)
block|{
name|createWrapperClass
argument_list|(
name|inf
argument_list|,
name|messageInfo
argument_list|,
name|opInfo
argument_list|,
name|method
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|wrapperBeans
return|;
block|}
specifier|private
name|void
name|createWrapperClass
parameter_list|(
name|MessagePartInfo
name|wrapperPart
parameter_list|,
name|MessageInfo
name|messageInfo
parameter_list|,
name|OperationInfo
name|op
parameter_list|,
name|Method
name|method
parameter_list|,
name|boolean
name|isRequest
parameter_list|)
block|{
name|QName
name|wrapperElement
init|=
name|messageInfo
operator|.
name|getName
argument_list|()
decl_stmt|;
name|boolean
name|anonymous
init|=
name|factory
operator|.
name|getAnonymousWrapperTypes
argument_list|()
decl_stmt|;
name|ClassWriter
name|cw
init|=
name|createClassWriter
argument_list|()
decl_stmt|;
name|String
name|pkg
init|=
name|getPackageName
argument_list|(
name|method
argument_list|)
operator|+
literal|".jaxws_asm"
operator|+
operator|(
name|anonymous
condition|?
literal|"_an"
else|:
literal|""
operator|)
decl_stmt|;
name|String
name|className
init|=
name|pkg
operator|+
literal|"."
operator|+
name|StringUtils
operator|.
name|capitalize
argument_list|(
name|op
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isRequest
condition|)
block|{
name|className
operator|=
name|className
operator|+
literal|"Response"
expr_stmt|;
block|}
name|String
name|pname
init|=
name|pkg
operator|+
literal|".package-info"
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|def
init|=
name|findClass
argument_list|(
name|pname
argument_list|,
name|method
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|def
operator|==
literal|null
condition|)
block|{
name|generatePackageInfo
argument_list|(
name|pname
argument_list|,
name|wrapperElement
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|method
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|def
operator|=
name|findClass
argument_list|(
name|className
argument_list|,
name|method
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|origClassName
init|=
name|className
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|def
operator|!=
literal|null
condition|)
block|{
name|Boolean
name|b
init|=
name|messageInfo
operator|.
name|getProperty
argument_list|(
literal|"parameterized"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|!=
literal|null
operator|&&
name|b
condition|)
block|{
name|className
operator|=
name|origClassName
operator|+
operator|(
operator|++
name|count
operator|)
expr_stmt|;
name|def
operator|=
name|findClass
argument_list|(
name|className
argument_list|,
name|method
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|wrapperPart
operator|.
name|setTypeClass
argument_list|(
name|def
argument_list|)
expr_stmt|;
name|wrapperBeans
operator|.
name|add
argument_list|(
name|def
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|String
name|classFileName
init|=
name|periodToSlashes
argument_list|(
name|className
argument_list|)
decl_stmt|;
name|cw
operator|.
name|visit
argument_list|(
name|Opcodes
operator|.
name|V1_5
argument_list|,
name|Opcodes
operator|.
name|ACC_PUBLIC
operator|+
name|Opcodes
operator|.
name|ACC_SUPER
argument_list|,
name|classFileName
argument_list|,
literal|null
argument_list|,
literal|"java/lang/Object"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|AnnotationVisitor
name|av0
init|=
name|cw
operator|.
name|visitAnnotation
argument_list|(
literal|"Ljavax/xml/bind/annotation/XmlRootElement;"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|av0
operator|.
name|visit
argument_list|(
literal|"name"
argument_list|,
name|wrapperElement
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visit
argument_list|(
literal|"namespace"
argument_list|,
name|wrapperElement
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
name|av0
operator|=
name|cw
operator|.
name|visitAnnotation
argument_list|(
literal|"Ljavax/xml/bind/annotation/XmlAccessorType;"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visitEnum
argument_list|(
literal|"value"
argument_list|,
literal|"Ljavax/xml/bind/annotation/XmlAccessType;"
argument_list|,
literal|"FIELD"
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
name|av0
operator|=
name|cw
operator|.
name|visitAnnotation
argument_list|(
literal|"Ljavax/xml/bind/annotation/XmlType;"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|anonymous
condition|)
block|{
name|av0
operator|.
name|visit
argument_list|(
literal|"name"
argument_list|,
name|wrapperElement
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visit
argument_list|(
literal|"namespace"
argument_list|,
name|wrapperElement
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|av0
operator|.
name|visit
argument_list|(
literal|"name"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
name|av0
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
comment|// add constructor
name|MethodVisitor
name|mv
init|=
name|cw
operator|.
name|visitMethod
argument_list|(
name|Opcodes
operator|.
name|ACC_PUBLIC
argument_list|,
literal|"<init>"
argument_list|,
literal|"()V"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|mv
operator|.
name|visitCode
argument_list|()
expr_stmt|;
name|Label
name|lbegin
init|=
operator|new
name|Label
argument_list|()
decl_stmt|;
name|mv
operator|.
name|visitLabel
argument_list|(
name|lbegin
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|ALOAD
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESPECIAL
argument_list|,
literal|"java/lang/Object"
argument_list|,
literal|"<init>"
argument_list|,
literal|"()V"
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitInsn
argument_list|(
name|Opcodes
operator|.
name|RETURN
argument_list|)
expr_stmt|;
name|Label
name|lend
init|=
operator|new
name|Label
argument_list|()
decl_stmt|;
name|mv
operator|.
name|visitLabel
argument_list|(
name|lend
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitLocalVariable
argument_list|(
literal|"this"
argument_list|,
literal|"L"
operator|+
name|classFileName
operator|+
literal|";"
argument_list|,
literal|null
argument_list|,
name|lbegin
argument_list|,
name|lend
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitMaxs
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
for|for
control|(
name|MessagePartInfo
name|mpi
range|:
name|messageInfo
operator|.
name|getMessageParts
argument_list|()
control|)
block|{
name|generateMessagePart
argument_list|(
name|cw
argument_list|,
name|mpi
argument_list|,
name|method
argument_list|,
name|classFileName
argument_list|)
expr_stmt|;
block|}
name|cw
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clz
init|=
name|loadClass
argument_list|(
name|className
argument_list|,
name|method
operator|.
name|getDeclaringClass
argument_list|()
argument_list|,
name|cw
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|wrapperPart
operator|.
name|setTypeClass
argument_list|(
name|clz
argument_list|)
expr_stmt|;
name|wrapperBeans
operator|.
name|add
argument_list|(
name|clz
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|generatePackageInfo
parameter_list|(
name|String
name|className
parameter_list|,
name|String
name|ns
parameter_list|,
name|Class
name|clz
parameter_list|)
block|{
name|ClassWriter
name|cw
init|=
name|createClassWriter
argument_list|()
decl_stmt|;
name|String
name|classFileName
init|=
name|periodToSlashes
argument_list|(
name|className
argument_list|)
decl_stmt|;
name|cw
operator|.
name|visit
argument_list|(
name|Opcodes
operator|.
name|V1_5
argument_list|,
name|Opcodes
operator|.
name|ACC_ABSTRACT
operator|+
name|Opcodes
operator|.
name|ACC_INTERFACE
argument_list|,
name|classFileName
argument_list|,
literal|null
argument_list|,
literal|"java/lang/Object"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|boolean
name|q
init|=
name|qualified
decl_stmt|;
name|SchemaInfo
name|si
init|=
name|interfaceInfo
operator|.
name|getService
argument_list|()
operator|.
name|getSchema
argument_list|(
name|ns
argument_list|)
decl_stmt|;
if|if
condition|(
name|si
operator|!=
literal|null
condition|)
block|{
name|q
operator|=
name|si
operator|.
name|isElementFormQualified
argument_list|()
expr_stmt|;
block|}
name|AnnotationVisitor
name|av0
init|=
name|cw
operator|.
name|visitAnnotation
argument_list|(
literal|"Ljavax/xml/bind/annotation/XmlSchema;"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|av0
operator|.
name|visit
argument_list|(
literal|"namespace"
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visitEnum
argument_list|(
literal|"elementFormDefault"
argument_list|,
name|getClassCode
argument_list|(
name|XmlNsForm
operator|.
name|class
argument_list|)
argument_list|,
name|q
condition|?
literal|"QUALIFIED"
else|:
literal|"UNQUALIFIED"
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
name|cw
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
name|loadClass
argument_list|(
name|className
argument_list|,
name|clz
argument_list|,
name|cw
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|generateMessagePart
parameter_list|(
name|ClassWriter
name|cw
parameter_list|,
name|MessagePartInfo
name|mpi
parameter_list|,
name|Method
name|method
parameter_list|,
name|String
name|className
parameter_list|)
block|{
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|mpi
operator|.
name|getProperty
argument_list|(
name|ReflectionServiceFactoryBean
operator|.
name|HEADER
argument_list|)
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|classFileName
init|=
name|periodToSlashes
argument_list|(
name|className
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|mpi
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|Class
name|clz
init|=
name|mpi
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
name|Object
name|obj
init|=
name|mpi
operator|.
name|getProperty
argument_list|(
name|ReflectionServiceFactoryBean
operator|.
name|RAW_CLASS
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
name|clz
operator|=
operator|(
name|Class
operator|)
name|obj
expr_stmt|;
block|}
name|Type
name|genericType
init|=
operator|(
name|Type
operator|)
name|mpi
operator|.
name|getProperty
argument_list|(
name|ReflectionServiceFactoryBean
operator|.
name|GENERIC_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|genericType
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|ParameterizedType
name|tp
init|=
operator|(
name|ParameterizedType
operator|)
name|genericType
decl_stmt|;
if|if
condition|(
name|tp
operator|.
name|getRawType
argument_list|()
operator|instanceof
name|Class
operator|&&
name|Holder
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
operator|(
name|Class
operator|)
name|tp
operator|.
name|getRawType
argument_list|()
argument_list|)
condition|)
block|{
name|genericType
operator|=
name|tp
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
expr_stmt|;
block|}
block|}
name|String
name|classCode
init|=
name|getClassCode
argument_list|(
name|clz
argument_list|)
decl_stmt|;
name|String
name|fieldDescriptor
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|genericType
operator|instanceof
name|ParameterizedType
condition|)
block|{
if|if
condition|(
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clz
argument_list|)
operator|||
name|clz
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|ParameterizedType
name|ptype
init|=
operator|(
name|ParameterizedType
operator|)
name|genericType
decl_stmt|;
name|Type
index|[]
name|types
init|=
name|ptype
operator|.
name|getActualTypeArguments
argument_list|()
decl_stmt|;
comment|// TODO: more complex Parameterized type
if|if
condition|(
name|types
operator|.
name|length
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|types
index|[
literal|0
index|]
operator|instanceof
name|Class
condition|)
block|{
name|fieldDescriptor
operator|=
name|getClassCode
argument_list|(
name|genericType
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|types
index|[
literal|0
index|]
operator|instanceof
name|GenericArrayType
condition|)
block|{
name|fieldDescriptor
operator|=
name|getClassCode
argument_list|(
name|genericType
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|types
index|[
literal|0
index|]
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|classCode
operator|=
name|getClassCode
argument_list|(
operator|(
operator|(
name|ParameterizedType
operator|)
name|types
index|[
literal|0
index|]
operator|)
operator|.
name|getRawType
argument_list|()
argument_list|)
expr_stmt|;
name|fieldDescriptor
operator|=
name|getClassCode
argument_list|(
name|genericType
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|classCode
operator|=
name|getClassCode
argument_list|(
operator|(
operator|(
name|ParameterizedType
operator|)
name|genericType
operator|)
operator|.
name|getRawType
argument_list|()
argument_list|)
expr_stmt|;
name|fieldDescriptor
operator|=
name|getClassCode
argument_list|(
name|genericType
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|fieldName
init|=
name|JavaUtils
operator|.
name|isJavaKeyword
argument_list|(
name|name
argument_list|)
condition|?
name|JavaUtils
operator|.
name|makeNonJavaKeyword
argument_list|(
name|name
argument_list|)
else|:
name|name
decl_stmt|;
name|FieldVisitor
name|fv
init|=
name|cw
operator|.
name|visitField
argument_list|(
name|Opcodes
operator|.
name|ACC_PRIVATE
argument_list|,
name|fieldName
argument_list|,
name|classCode
argument_list|,
name|fieldDescriptor
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Annotation
argument_list|>
name|jaxbAnnos
init|=
name|getJaxbAnnos
argument_list|(
name|mpi
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|addJAXBAnnotations
argument_list|(
name|fv
argument_list|,
name|jaxbAnnos
argument_list|)
condition|)
block|{
name|AnnotationVisitor
name|av0
init|=
name|fv
operator|.
name|visitAnnotation
argument_list|(
literal|"Ljavax/xml/bind/annotation/XmlElement;"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|av0
operator|.
name|visit
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|factory
operator|.
name|isWrapperPartQualified
argument_list|(
name|mpi
argument_list|)
condition|)
block|{
name|av0
operator|.
name|visit
argument_list|(
literal|"namespace"
argument_list|,
name|mpi
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|factory
operator|.
name|isWrapperPartNillable
argument_list|(
name|mpi
argument_list|)
condition|)
block|{
name|av0
operator|.
name|visit
argument_list|(
literal|"nillable"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|factory
operator|.
name|getWrapperPartMinOccurs
argument_list|(
name|mpi
argument_list|)
operator|==
literal|1
condition|)
block|{
name|av0
operator|.
name|visit
argument_list|(
literal|"required"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|av0
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
block|}
name|fv
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
name|String
name|methodName
init|=
name|JAXBUtils
operator|.
name|nameToIdentifier
argument_list|(
name|name
argument_list|,
name|JAXBUtils
operator|.
name|IdentifierType
operator|.
name|GETTER
argument_list|)
decl_stmt|;
name|MethodVisitor
name|mv
init|=
name|cw
operator|.
name|visitMethod
argument_list|(
name|Opcodes
operator|.
name|ACC_PUBLIC
argument_list|,
name|methodName
argument_list|,
literal|"()"
operator|+
name|classCode
argument_list|,
name|fieldDescriptor
operator|==
literal|null
condition|?
literal|null
else|:
literal|"()"
operator|+
name|fieldDescriptor
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|mv
operator|.
name|visitCode
argument_list|()
expr_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|ALOAD
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitFieldInsn
argument_list|(
name|Opcodes
operator|.
name|GETFIELD
argument_list|,
name|classFileName
argument_list|,
name|fieldName
argument_list|,
name|classCode
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitInsn
argument_list|(
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|Type
operator|.
name|getType
argument_list|(
name|classCode
argument_list|)
operator|.
name|getOpcode
argument_list|(
name|Opcodes
operator|.
name|IRETURN
argument_list|)
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitMaxs
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
name|methodName
operator|=
name|JAXBUtils
operator|.
name|nameToIdentifier
argument_list|(
name|name
argument_list|,
name|JAXBUtils
operator|.
name|IdentifierType
operator|.
name|SETTER
argument_list|)
expr_stmt|;
name|mv
operator|=
name|cw
operator|.
name|visitMethod
argument_list|(
name|Opcodes
operator|.
name|ACC_PUBLIC
argument_list|,
name|methodName
argument_list|,
literal|"("
operator|+
name|classCode
operator|+
literal|")V"
argument_list|,
name|fieldDescriptor
operator|==
literal|null
condition|?
literal|null
else|:
literal|"("
operator|+
name|fieldDescriptor
operator|+
literal|")V"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitCode
argument_list|()
expr_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|ALOAD
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|Type
name|setType
init|=
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|Type
operator|.
name|getType
argument_list|(
name|classCode
argument_list|)
decl_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|setType
operator|.
name|getOpcode
argument_list|(
name|Opcodes
operator|.
name|ILOAD
argument_list|)
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitFieldInsn
argument_list|(
name|Opcodes
operator|.
name|PUTFIELD
argument_list|,
name|className
argument_list|,
name|fieldName
argument_list|,
name|classCode
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitInsn
argument_list|(
name|Opcodes
operator|.
name|RETURN
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitMaxs
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
block|}
specifier|private
name|boolean
name|addJAXBAnnotations
parameter_list|(
name|FieldVisitor
name|fv
parameter_list|,
name|List
argument_list|<
name|Annotation
argument_list|>
name|jaxbAnnos
parameter_list|)
block|{
name|AnnotationVisitor
name|av0
decl_stmt|;
name|boolean
name|addedEl
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Annotation
name|ann
range|:
name|jaxbAnnos
control|)
block|{
if|if
condition|(
name|ann
operator|instanceof
name|XmlMimeType
condition|)
block|{
name|av0
operator|=
name|fv
operator|.
name|visitAnnotation
argument_list|(
literal|"Ljavax/xml/bind/annotation/XmlMimeType;"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visit
argument_list|(
literal|"value"
argument_list|,
operator|(
operator|(
name|XmlMimeType
operator|)
name|ann
operator|)
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ann
operator|instanceof
name|XmlJavaTypeAdapter
condition|)
block|{
name|av0
operator|=
name|fv
operator|.
name|visitAnnotation
argument_list|(
literal|"Ljavax/xml/bind/annotation/adapters/XmlJavaTypeAdapter;"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|XmlJavaTypeAdapter
name|adapter
init|=
operator|(
name|XmlJavaTypeAdapter
operator|)
name|ann
decl_stmt|;
if|if
condition|(
name|adapter
operator|.
name|value
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|av0
operator|.
name|visit
argument_list|(
literal|"value"
argument_list|,
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|Type
operator|.
name|getType
argument_list|(
name|getClassCode
argument_list|(
name|adapter
operator|.
name|value
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|adapter
operator|.
name|type
argument_list|()
operator|!=
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|XmlJavaTypeAdapter
operator|.
name|DEFAULT
operator|.
name|class
condition|)
block|{
name|av0
operator|.
name|visit
argument_list|(
literal|"type"
argument_list|,
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|Type
operator|.
name|getType
argument_list|(
name|getClassCode
argument_list|(
name|adapter
operator|.
name|type
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|av0
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ann
operator|instanceof
name|XmlAttachmentRef
condition|)
block|{
name|av0
operator|=
name|fv
operator|.
name|visitAnnotation
argument_list|(
literal|"Ljavax/xml/bind/annotation/XmlAttachmentRef;"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ann
operator|instanceof
name|XmlList
condition|)
block|{
name|av0
operator|=
name|fv
operator|.
name|visitAnnotation
argument_list|(
literal|"Ljavax/xml/bind/annotation/XmlList;"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ann
operator|instanceof
name|XmlElement
condition|)
block|{
name|addedEl
operator|=
literal|true
expr_stmt|;
name|XmlElement
name|el
init|=
operator|(
name|XmlElement
operator|)
name|ann
decl_stmt|;
name|av0
operator|=
name|fv
operator|.
name|visitAnnotation
argument_list|(
literal|"Ljavax/xml/bind/annotation/XmlElement;"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visit
argument_list|(
literal|"name"
argument_list|,
name|el
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visit
argument_list|(
literal|"nillable"
argument_list|,
name|el
operator|.
name|nillable
argument_list|()
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visit
argument_list|(
literal|"requried"
argument_list|,
name|el
operator|.
name|required
argument_list|()
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visit
argument_list|(
literal|"namespace"
argument_list|,
name|el
operator|.
name|namespace
argument_list|()
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visit
argument_list|(
literal|"defaultValue"
argument_list|,
name|el
operator|.
name|defaultValue
argument_list|()
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visit
argument_list|(
literal|"type"
argument_list|,
name|el
operator|.
name|type
argument_list|()
argument_list|)
expr_stmt|;
name|av0
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|addedEl
return|;
block|}
block|}
end_class

end_unit

