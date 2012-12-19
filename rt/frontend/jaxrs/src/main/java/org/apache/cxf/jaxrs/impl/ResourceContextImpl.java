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
name|impl
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|InternalServerErrorException
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
name|container
operator|.
name|ResourceContext
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|model
operator|.
name|ClassResourceInfo
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
name|model
operator|.
name|OperationResourceInfo
import|;
end_import

begin_class
specifier|public
class|class
name|ResourceContextImpl
implements|implements
name|ResourceContext
block|{
specifier|private
name|ClassResourceInfo
name|cri
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|subClass
decl_stmt|;
specifier|public
name|ResourceContextImpl
parameter_list|(
name|OperationResourceInfo
name|ori
parameter_list|)
block|{
name|this
operator|.
name|cri
operator|=
name|ori
operator|.
name|getClassResourceInfo
argument_list|()
expr_stmt|;
name|this
operator|.
name|subClass
operator|=
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getReturnType
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getResource
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
name|T
name|resource
init|=
literal|null
decl_stmt|;
try|try
block|{
name|resource
operator|=
name|cls
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|InternalServerErrorException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
name|doInitResource
argument_list|(
name|cls
argument_list|,
name|resource
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|initResource
parameter_list|(
name|T
name|resource
parameter_list|)
block|{
return|return
name|doInitResource
argument_list|(
name|resource
operator|.
name|getClass
argument_list|()
argument_list|,
name|resource
argument_list|)
return|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|doInitResource
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|T
name|resource
parameter_list|)
block|{
name|cri
operator|.
name|getSubResource
argument_list|(
name|subClass
argument_list|,
name|cls
argument_list|,
name|resource
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|resource
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|matchResource
parameter_list|(
name|URI
name|arg0
parameter_list|)
throws|throws
name|NullPointerException
throws|,
name|IllegalArgumentException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|matchResource
parameter_list|(
name|URI
name|arg0
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|arg1
parameter_list|)
throws|throws
name|NullPointerException
throws|,
name|IllegalArgumentException
throws|,
name|ClassCastException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|UriInfo
name|matchUriInfo
parameter_list|(
name|URI
name|arg0
parameter_list|)
throws|throws
name|NullPointerException
throws|,
name|IllegalArgumentException
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

