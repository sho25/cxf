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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|sso
operator|.
name|state
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
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
name|DELETE
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
name|Encoded
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
name|GET
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
name|POST
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
name|Path
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
name|PathParam
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
name|Produces
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"state"
argument_list|)
specifier|public
class|class
name|HTTPSPStateManager
implements|implements
name|SPStateManager
block|{
specifier|private
name|MemorySPStateManager
name|manager
init|=
operator|new
name|MemorySPStateManager
argument_list|()
decl_stmt|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/request/{relayState}"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"application/xml"
argument_list|)
specifier|public
name|void
name|setRequestState
parameter_list|(
annotation|@
name|Encoded
annotation|@
name|PathParam
argument_list|(
literal|"relayState"
argument_list|)
name|String
name|relayState
parameter_list|,
name|RequestState
name|state
parameter_list|)
block|{
name|manager
operator|.
name|setRequestState
argument_list|(
name|relayState
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
annotation|@
name|DELETE
annotation|@
name|Path
argument_list|(
literal|"/request/{relayState}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
specifier|public
name|RequestState
name|removeRequestState
parameter_list|(
annotation|@
name|Encoded
annotation|@
name|PathParam
argument_list|(
literal|"relayState"
argument_list|)
name|String
name|relayState
parameter_list|)
block|{
return|return
name|manager
operator|.
name|removeRequestState
argument_list|(
name|relayState
argument_list|)
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/response/{contextKey}"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"application/xml"
argument_list|)
specifier|public
name|void
name|setResponseState
parameter_list|(
annotation|@
name|Encoded
annotation|@
name|PathParam
argument_list|(
literal|"contextKey"
argument_list|)
name|String
name|contextKey
parameter_list|,
name|ResponseState
name|state
parameter_list|)
block|{
name|manager
operator|.
name|setResponseState
argument_list|(
name|contextKey
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/response/{contextKey}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
specifier|public
name|ResponseState
name|getResponseState
parameter_list|(
annotation|@
name|Encoded
annotation|@
name|PathParam
argument_list|(
literal|"contextKey"
argument_list|)
name|String
name|contextKey
parameter_list|)
block|{
return|return
name|manager
operator|.
name|getResponseState
argument_list|(
name|contextKey
argument_list|)
return|;
block|}
annotation|@
name|DELETE
annotation|@
name|Path
argument_list|(
literal|"/response/{contextKey}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
specifier|public
name|ResponseState
name|removeResponseState
parameter_list|(
name|String
name|contextKey
parameter_list|)
block|{
return|return
name|manager
operator|.
name|getResponseState
argument_list|(
name|contextKey
argument_list|)
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"close"
argument_list|)
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
comment|// TODO Auto-generated method stub
block|}
block|}
end_class

end_unit

