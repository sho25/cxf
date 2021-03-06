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
name|tracing
operator|.
name|brave
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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

begin_interface
specifier|public
interface|interface
name|HttpAdapterFactory
block|{
specifier|final
class|class
name|Request
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
decl_stmt|;
specifier|private
specifier|final
name|URI
name|uri
decl_stmt|;
specifier|private
specifier|final
name|String
name|method
decl_stmt|;
name|Request
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|,
name|URI
name|uri
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|()
block|{
return|return
name|headers
return|;
block|}
name|URI
name|uri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
name|String
name|method
parameter_list|()
block|{
return|return
name|method
return|;
block|}
block|}
specifier|final
class|class
name|Response
block|{
specifier|private
specifier|final
name|Integer
name|status
decl_stmt|;
name|Response
parameter_list|(
name|Integer
name|status
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
block|}
name|Integer
name|status
parameter_list|()
block|{
return|return
name|status
return|;
block|}
block|}
specifier|static
name|Request
name|request
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|,
name|URI
name|uri
parameter_list|,
name|String
name|method
parameter_list|)
block|{
return|return
operator|new
name|Request
argument_list|(
name|headers
argument_list|,
name|uri
argument_list|,
name|method
argument_list|)
return|;
block|}
specifier|static
name|Response
name|response
parameter_list|(
name|Integer
name|status
parameter_list|)
block|{
return|return
operator|new
name|Response
argument_list|(
name|status
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

