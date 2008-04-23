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
name|transport
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLConnection
import|;
end_import

begin_comment
comment|/**  * This class is returned from the URLConnectionFactory to give  * information that is from the URLConnection that was created by that  * factory.  */
end_comment

begin_class
specifier|public
class|class
name|URLConnectionInfo
block|{
comment|/**      * The URL the connection is associated with.      */
specifier|protected
specifier|final
name|URL
name|theURL
decl_stmt|;
comment|/**      * This constructor is used to represent a URLConnection.      *       * @param connection The URLConnection that this info object will represent.      */
specifier|public
name|URLConnectionInfo
parameter_list|(
name|URLConnection
name|connection
parameter_list|)
block|{
name|theURL
operator|=
name|connection
operator|.
name|getURL
argument_list|()
expr_stmt|;
block|}
comment|/**      * This field returns the URL associated with the connection      * in question.      *       * @return      */
specifier|public
name|URL
name|getURL
parameter_list|()
block|{
return|return
name|theURL
return|;
block|}
block|}
end_class

end_unit

