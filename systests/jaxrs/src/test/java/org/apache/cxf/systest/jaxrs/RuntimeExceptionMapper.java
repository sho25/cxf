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
name|systest
operator|.
name|jaxrs
package|;
end_package

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
name|Context
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
name|Response
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
name|UriInfo
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
name|ext
operator|.
name|ExceptionMapper
import|;
end_import

begin_class
specifier|public
class|class
name|RuntimeExceptionMapper
implements|implements
name|ExceptionMapper
argument_list|<
name|RuntimeException
argument_list|>
block|{
annotation|@
name|Context
specifier|private
name|UriInfo
name|ui
decl_stmt|;
specifier|public
name|Response
name|toResponse
parameter_list|(
name|RuntimeException
name|exception
parameter_list|)
block|{
name|String
name|path
init|=
name|ui
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
literal|"nonexistent"
argument_list|)
condition|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
literal|405
argument_list|)
operator|.
name|type
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|entity
argument_list|(
literal|"Nonexistent method"
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

