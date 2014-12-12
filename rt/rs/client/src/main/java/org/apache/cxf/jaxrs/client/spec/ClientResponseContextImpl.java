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
name|jaxrs
operator|.
name|client
operator|.
name|spec
package|;
end_package

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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientResponseContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
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
name|jaxrs
operator|.
name|impl
operator|.
name|AbstractResponseContextImpl
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
name|jaxrs
operator|.
name|impl
operator|.
name|ResponseImpl
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
name|jaxrs
operator|.
name|utils
operator|.
name|ExceptionUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|HttpUtils
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

begin_class
specifier|public
class|class
name|ClientResponseContextImpl
extends|extends
name|AbstractResponseContextImpl
implements|implements
name|ClientResponseContext
block|{
specifier|public
name|ClientResponseContextImpl
parameter_list|(
name|ResponseImpl
name|r
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|super
argument_list|(
name|r
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
specifier|public
name|InputStream
name|getEntityStream
parameter_list|()
block|{
name|InputStream
name|is
init|=
name|m
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|is
operator|=
operator|(
operator|(
name|ResponseImpl
operator|)
name|r
operator|)
operator|.
name|convertEntityToStreamIfPossible
argument_list|()
expr_stmt|;
block|}
return|return
name|is
return|;
block|}
annotation|@
name|Override
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
name|HttpUtils
operator|.
name|getModifiableStringHeaders
argument_list|(
name|m
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setEntityStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
name|m
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|r
operator|.
name|setEntity
argument_list|(
name|is
argument_list|,
name|r
operator|.
name|getEntityAnnotations
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasEntity
parameter_list|()
block|{
try|try
block|{
return|return
operator|!
name|IOUtils
operator|.
name|isEmpty
argument_list|(
name|getEntityStream
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
name|ex
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

