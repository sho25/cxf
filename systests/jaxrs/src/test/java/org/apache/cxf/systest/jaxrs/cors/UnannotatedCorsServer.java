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
operator|.
name|cors
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
name|PUT
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
name|cors
operator|.
name|CrossOriginResourceSharing
import|;
end_import

begin_comment
comment|/**  * Service bean with no class-level annotation for cross-script control.  */
end_comment

begin_class
specifier|public
class|class
name|UnannotatedCorsServer
block|{
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"/simpleGet/{echo}"
argument_list|)
specifier|public
name|String
name|simpleGet
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"echo"
argument_list|)
name|String
name|echo
parameter_list|)
block|{
return|return
name|echo
return|;
block|}
annotation|@
name|DELETE
annotation|@
name|Path
argument_list|(
literal|"/delete"
argument_list|)
specifier|public
name|Response
name|deleteSomething
parameter_list|()
block|{
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|CrossOriginResourceSharing
argument_list|(
name|allowOrigins
operator|=
block|{
literal|"http://area51.mil:31415"
block|}
argument_list|,
name|allowCredentials
operator|=
literal|true
argument_list|,
name|exposeHeaders
operator|=
block|{
literal|"X-custom-3"
block|,
literal|"X-custom-4"
block|}
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"/annotatedGet/{echo}"
argument_list|)
specifier|public
name|String
name|annotatedGet
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"echo"
argument_list|)
name|String
name|echo
parameter_list|)
block|{
return|return
name|echo
return|;
block|}
comment|/**      * A method annotated to test preflight.      * @param input      * @return      */
annotation|@
name|PUT
annotation|@
name|Consumes
argument_list|(
literal|"text/plain"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"/annotatedPut"
argument_list|)
annotation|@
name|CrossOriginResourceSharing
argument_list|(
name|allowOrigins
operator|=
block|{
literal|"http://area51.mil:31415"
block|}
argument_list|,
name|allowCredentials
operator|=
literal|true
argument_list|,
name|maxAge
operator|=
literal|1
argument_list|,
name|allowHeaders
operator|=
block|{
literal|"X-custom-1"
block|,
literal|"X-custom-2"
block|}
argument_list|,
name|exposeHeaders
operator|=
block|{
literal|"X-custom-3"
block|,
literal|"X-custom-4"
block|}
argument_list|)
specifier|public
name|String
name|annotatedPut
parameter_list|(
name|String
name|input
parameter_list|)
block|{
return|return
name|input
return|;
block|}
block|}
end_class

end_unit

