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
name|xsdvalidation
package|;
end_package

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
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|transform
operator|.
name|Transformer
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
name|TransformerException
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
name|TransformerFactory
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
name|dom
operator|.
name|DOMSource
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
name|StreamResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|ls
operator|.
name|LSInput
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

begin_comment
comment|/**  *   */
end_comment

begin_class
class|class
name|DOMLSInput
implements|implements
name|LSInput
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
name|DOMLSInput
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|systemId
decl_stmt|;
specifier|private
name|String
name|data
decl_stmt|;
name|DOMLSInput
parameter_list|(
name|Document
name|doc
parameter_list|,
name|String
name|systemId
parameter_list|)
throws|throws
name|TransformerException
block|{
name|this
operator|.
name|systemId
operator|=
name|systemId
expr_stmt|;
name|TransformerFactory
name|factory
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Transformer
name|transformer
init|=
name|factory
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|DOMSource
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|source
operator|.
name|setSystemId
argument_list|(
name|systemId
argument_list|)
expr_stmt|;
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|StreamResult
name|result
init|=
operator|new
name|StreamResult
argument_list|(
name|writer
argument_list|)
decl_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
name|source
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|data
operator|=
name|writer
operator|.
name|toString
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|systemId
operator|+
literal|": "
operator|+
name|data
argument_list|)
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|String
name|getBaseURI
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|InputStream
name|getByteStream
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|boolean
name|getCertifiedText
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|Reader
name|getCharacterStream
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
literal|"utf-8"
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|String
name|getPublicId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|String
name|getStringData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|String
name|getSystemId
parameter_list|()
block|{
return|return
name|systemId
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setBaseURI
parameter_list|(
name|String
name|baseURI
parameter_list|)
block|{     }
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setByteStream
parameter_list|(
name|InputStream
name|byteStream
parameter_list|)
block|{     }
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setCertifiedText
parameter_list|(
name|boolean
name|certifiedText
parameter_list|)
block|{     }
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setCharacterStream
parameter_list|(
name|Reader
name|characterStream
parameter_list|)
block|{     }
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{     }
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setPublicId
parameter_list|(
name|String
name|publicId
parameter_list|)
block|{     }
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setStringData
parameter_list|(
name|String
name|stringData
parameter_list|)
block|{     }
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|setSystemId
parameter_list|(
name|String
name|systemId
parameter_list|)
block|{     }
block|}
end_class

end_unit

