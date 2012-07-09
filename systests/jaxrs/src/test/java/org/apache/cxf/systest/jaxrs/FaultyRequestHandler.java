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
name|MultivaluedMap
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|RequestHandler
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
name|message
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|FaultyRequestHandler
implements|implements
name|RequestHandler
block|{
annotation|@
name|Context
specifier|private
name|UriInfo
name|uriInfo
decl_stmt|;
specifier|public
name|Response
name|handleRequest
parameter_list|(
name|Message
name|m
parameter_list|,
name|ClassResourceInfo
name|resourceClass
parameter_list|)
block|{
if|if
condition|(
name|uriInfo
operator|.
name|getPath
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"/propogateExceptionVar/1"
argument_list|)
condition|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|vars
init|=
name|uriInfo
operator|.
name|getPathParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|vars
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|vars
operator|.
name|get
argument_list|(
literal|"i"
argument_list|)
operator|!=
literal|null
operator|&&
name|vars
operator|.
name|get
argument_list|(
literal|"i"
argument_list|)
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
literal|"1"
operator|.
name|equals
argument_list|(
name|vars
operator|.
name|getFirst
argument_list|(
literal|"i"
argument_list|)
argument_list|)
condition|)
block|{
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.systest.for-out-fault-interceptor"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

