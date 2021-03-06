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
operator|.
name|interceptors
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Graphics
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Image
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|MediaTracker
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|BufferedImage
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedExceptionAction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Map
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
name|UUID
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|stream
operator|.
name|ImageOutputStream
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
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|attachment
operator|.
name|AttachmentImpl
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
name|attachment
operator|.
name|ByteDataSource
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|AbstractSoapInterceptor
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
name|binding
operator|.
name|soap
operator|.
name|model
operator|.
name|SoapBodyInfo
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
name|databinding
operator|.
name|DataBinding
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
name|CastUtils
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
name|IOUtils
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
name|LoadingByteArrayOutputStream
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
name|interceptor
operator|.
name|AttachmentOutInterceptor
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
name|interceptor
operator|.
name|Fault
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
name|JAXBDataBinding
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
name|message
operator|.
name|Attachment
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
name|message
operator|.
name|Exchange
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
name|message
operator|.
name|Message
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
name|phase
operator|.
name|Phase
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
name|Service
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
name|BindingMessageInfo
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
name|BindingOperationInfo
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_class
specifier|public
class|class
name|SwAOutInterceptor
extends|extends
name|AbstractSoapInterceptor
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
name|SwAOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Method
argument_list|>
name|SWA_REF_METHOD
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
literal|4
argument_list|,
literal|0.75f
argument_list|,
literal|2
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|SWA_REF_NO_METHOD
init|=
name|Collections
operator|.
name|newSetFromMap
argument_list|(
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|(
literal|4
argument_list|,
literal|0.75f
argument_list|,
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|AttachmentOutInterceptor
name|attachOut
init|=
operator|new
name|AttachmentOutInterceptor
argument_list|()
decl_stmt|;
specifier|public
name|SwAOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_LOGICAL
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|HolderOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|WrapperClassOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|callSWARefMethod
parameter_list|(
specifier|final
name|JAXBContext
name|ctx
parameter_list|)
block|{
name|String
name|cname
init|=
name|ctx
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Method
name|m
init|=
name|SWA_REF_METHOD
operator|.
name|get
argument_list|(
name|cname
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
operator|&&
operator|!
name|SWA_REF_NO_METHOD
operator|.
name|contains
argument_list|(
name|cname
argument_list|)
condition|)
block|{
try|try
block|{
name|m
operator|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedExceptionAction
argument_list|<
name|Method
argument_list|>
argument_list|()
block|{
specifier|public
name|Method
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|Method
name|hasSwaRefMethod
init|=
name|ctx
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"hasSwaRef"
argument_list|,
operator|new
name|Class
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|hasSwaRefMethod
operator|.
name|isAccessible
argument_list|()
condition|)
block|{
name|hasSwaRefMethod
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|hasSwaRefMethod
return|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|SWA_REF_NO_METHOD
operator|.
name|add
argument_list|(
name|cname
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SWA_REF_METHOD
operator|.
name|put
argument_list|(
name|cname
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
try|try
block|{
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|Boolean
operator|)
name|m
operator|.
name|invoke
argument_list|(
name|ctx
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|BindingOperationInfo
name|bop
init|=
name|ex
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|bop
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|bop
operator|.
name|isUnwrapped
argument_list|()
condition|)
block|{
name|bop
operator|=
name|bop
operator|.
name|getWrappedOperation
argument_list|()
expr_stmt|;
block|}
name|boolean
name|client
init|=
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|BindingMessageInfo
name|bmi
init|=
name|client
condition|?
name|bop
operator|.
name|getInput
argument_list|()
else|:
name|bop
operator|.
name|getOutput
argument_list|()
decl_stmt|;
if|if
condition|(
name|bmi
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|SoapBodyInfo
name|sbi
init|=
name|bmi
operator|.
name|getExtensor
argument_list|(
name|SoapBodyInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|sbi
operator|==
literal|null
operator|||
name|sbi
operator|.
name|getAttachments
argument_list|()
operator|==
literal|null
operator|||
name|sbi
operator|.
name|getAttachments
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Service
name|s
init|=
name|ex
operator|.
name|getService
argument_list|()
decl_stmt|;
name|DataBinding
name|db
init|=
name|s
operator|.
name|getDataBinding
argument_list|()
decl_stmt|;
if|if
condition|(
name|db
operator|instanceof
name|JAXBDataBinding
operator|&&
name|hasSwaRef
argument_list|(
operator|(
name|JAXBDataBinding
operator|)
name|db
argument_list|)
condition|)
block|{
name|setupAttachmentOutput
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
name|processAttachments
argument_list|(
name|message
argument_list|,
name|sbi
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|processAttachments
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|SoapBodyInfo
name|sbi
parameter_list|)
block|{
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|atts
init|=
name|setupAttachmentOutput
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|outObjects
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|message
operator|.
name|getContent
argument_list|(
name|List
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|MessagePartInfo
name|mpi
range|:
name|sbi
operator|.
name|getAttachments
argument_list|()
control|)
block|{
name|String
name|partName
init|=
name|mpi
operator|.
name|getConcreteName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|String
name|ct
init|=
operator|(
name|String
operator|)
name|mpi
operator|.
name|getProperty
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
name|String
name|id
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|partName
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"@apache.org"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// this assumes things are in order...
name|int
name|idx
init|=
name|mpi
operator|.
name|getIndex
argument_list|()
decl_stmt|;
name|Object
name|o
init|=
name|outObjects
operator|.
name|get
argument_list|(
name|idx
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|outObjects
operator|.
name|set
argument_list|(
name|idx
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|DataHandler
name|dh
init|=
literal|null
decl_stmt|;
comment|// This code could probably be refactored out somewhere...
if|if
condition|(
name|o
operator|instanceof
name|Source
condition|)
block|{
name|dh
operator|=
operator|new
name|DataHandler
argument_list|(
name|createDataSource
argument_list|(
operator|(
name|Source
operator|)
name|o
argument_list|,
name|ct
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|Image
condition|)
block|{
specifier|final
name|Image
name|img
init|=
operator|(
name|Image
operator|)
name|o
decl_stmt|;
specifier|final
name|String
name|contentType
init|=
name|ct
decl_stmt|;
name|dh
operator|=
operator|new
name|DataHandler
argument_list|(
name|o
argument_list|,
name|ct
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|LoadingByteArrayOutputStream
name|bout
init|=
operator|new
name|LoadingByteArrayOutputStream
argument_list|()
decl_stmt|;
name|writeTo
argument_list|(
name|bout
argument_list|)
expr_stmt|;
return|return
name|bout
operator|.
name|createInputStream
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeTo
parameter_list|(
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|ImageWriter
name|writer
init|=
literal|null
decl_stmt|;
name|Iterator
argument_list|<
name|ImageWriter
argument_list|>
name|writers
init|=
name|ImageIO
operator|.
name|getImageWritersByMIMEType
argument_list|(
name|contentType
argument_list|)
decl_stmt|;
if|if
condition|(
name|writers
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|writer
operator|=
name|writers
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|BufferedImage
name|bimg
init|=
name|convertToBufferedImage
argument_list|(
name|img
argument_list|)
decl_stmt|;
name|ImageOutputStream
name|iout
init|=
name|ImageIO
operator|.
name|createImageOutputStream
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|writer
operator|.
name|setOutput
argument_list|(
name|iout
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|bimg
argument_list|)
expr_stmt|;
name|writer
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|iout
operator|.
name|flush
argument_list|()
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|DataHandler
condition|)
block|{
name|dh
operator|=
operator|(
name|DataHandler
operator|)
name|o
expr_stmt|;
name|ct
operator|=
name|dh
operator|.
name|getContentType
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
literal|"text/xml"
operator|.
name|equals
argument_list|(
name|ct
argument_list|)
operator|&&
name|dh
operator|.
name|getContent
argument_list|()
operator|instanceof
name|Source
condition|)
block|{
name|dh
operator|=
operator|new
name|DataHandler
argument_list|(
name|createDataSource
argument_list|(
operator|(
name|Source
operator|)
name|dh
operator|.
name|getContent
argument_list|()
argument_list|,
name|ct
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ignore, use same dh
block|}
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|byte
index|[]
condition|)
block|{
if|if
condition|(
name|ct
operator|==
literal|null
condition|)
block|{
name|ct
operator|=
literal|"application/octet-stream"
expr_stmt|;
block|}
name|dh
operator|=
operator|new
name|DataHandler
argument_list|(
operator|new
name|ByteDataSource
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|o
argument_list|,
name|ct
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
if|if
condition|(
name|ct
operator|==
literal|null
condition|)
block|{
name|ct
operator|=
literal|"text/plain; charset=\'UTF-8\'"
expr_stmt|;
block|}
name|dh
operator|=
operator|new
name|DataHandler
argument_list|(
operator|new
name|ByteDataSource
argument_list|(
operator|(
operator|(
name|String
operator|)
name|o
operator|)
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|,
name|ct
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"ATTACHMENT_NOT_SUPPORTED"
argument_list|,
name|LOG
argument_list|,
name|o
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|AttachmentImpl
name|att
init|=
operator|new
name|AttachmentImpl
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|att
operator|.
name|setDataHandler
argument_list|(
name|dh
argument_list|)
expr_stmt|;
name|att
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
name|ct
argument_list|)
expr_stmt|;
name|att
operator|.
name|setHeader
argument_list|(
literal|"Content-ID"
argument_list|,
literal|"<"
operator|+
name|id
operator|+
literal|">"
argument_list|)
expr_stmt|;
name|atts
operator|.
name|add
argument_list|(
name|att
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|boolean
name|hasSwaRef
parameter_list|(
name|JAXBDataBinding
name|db
parameter_list|)
block|{
name|JAXBContext
name|context
init|=
name|db
operator|.
name|getContext
argument_list|()
decl_stmt|;
return|return
name|callSWARefMethod
argument_list|(
name|context
argument_list|)
return|;
block|}
specifier|private
name|DataSource
name|createDataSource
parameter_list|(
name|Source
name|o
parameter_list|,
name|String
name|ct
parameter_list|)
block|{
name|DataSource
name|ds
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|StreamSource
condition|)
block|{
name|StreamSource
name|src
init|=
operator|(
name|StreamSource
operator|)
name|o
decl_stmt|;
try|try
block|{
if|if
condition|(
name|src
operator|.
name|getInputStream
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
init|(
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
literal|2048
argument_list|)
init|)
block|{
name|IOUtils
operator|.
name|copy
argument_list|(
name|src
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|bos
argument_list|,
literal|1024
argument_list|)
expr_stmt|;
name|ds
operator|=
operator|new
name|ByteDataSource
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|ct
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ds
operator|=
operator|new
name|ByteDataSource
argument_list|(
name|IOUtils
operator|.
name|toString
argument_list|(
name|src
operator|.
name|getReader
argument_list|()
argument_list|)
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|,
name|ct
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|ByteArrayOutputStream
name|bwriter
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|writer
operator|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|bwriter
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|o
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|ds
operator|=
operator|new
name|ByteDataSource
argument_list|(
name|bwriter
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|ct
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e1
argument_list|)
throw|;
block|}
finally|finally
block|{
name|StaxUtils
operator|.
name|close
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ds
return|;
block|}
specifier|private
name|BufferedImage
name|convertToBufferedImage
parameter_list|(
name|Image
name|image
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|image
operator|instanceof
name|BufferedImage
condition|)
block|{
return|return
operator|(
name|BufferedImage
operator|)
name|image
return|;
block|}
comment|// Wait until the image is completely loaded
name|MediaTracker
name|tracker
init|=
operator|new
name|MediaTracker
argument_list|(
operator|new
name|Component
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|6412221228374321325L
decl_stmt|;
block|}
argument_list|)
decl_stmt|;
name|tracker
operator|.
name|addImage
argument_list|(
name|image
argument_list|,
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|tracker
operator|.
name|waitForAll
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// Create a BufferedImage so we can write it out later
name|BufferedImage
name|bufImage
init|=
operator|new
name|BufferedImage
argument_list|(
name|image
operator|.
name|getWidth
argument_list|(
literal|null
argument_list|)
argument_list|,
name|image
operator|.
name|getHeight
argument_list|(
literal|null
argument_list|)
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_ARGB
argument_list|)
decl_stmt|;
name|Graphics
name|g
init|=
name|bufImage
operator|.
name|createGraphics
argument_list|()
decl_stmt|;
name|g
operator|.
name|drawImage
argument_list|(
name|image
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|bufImage
return|;
block|}
specifier|private
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|setupAttachmentOutput
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
comment|// We have attachments, so add the interceptor
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|attachOut
argument_list|)
expr_stmt|;
comment|// We should probably come up with another property for this
name|message
operator|.
name|put
argument_list|(
name|AttachmentOutInterceptor
operator|.
name|WRITE_ATTACHMENTS
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|atts
init|=
name|message
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
if|if
condition|(
name|atts
operator|==
literal|null
condition|)
block|{
name|atts
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|message
operator|.
name|setAttachments
argument_list|(
name|atts
argument_list|)
expr_stmt|;
block|}
return|return
name|atts
return|;
block|}
block|}
end_class

end_unit

