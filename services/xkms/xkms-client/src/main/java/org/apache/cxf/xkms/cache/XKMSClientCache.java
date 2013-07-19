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
name|xkms
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

begin_interface
specifier|public
interface|interface
name|XKMSClientCache
extends|extends
name|Closeable
block|{
comment|/**      * Store an X509Certificate in the Cache      */
name|void
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|X509Certificate
name|certificate
parameter_list|)
function_decl|;
comment|/**      * Get an X509Certificate from the cache matching the given key. Returns null if there      * is no such certificate in the cache.      */
name|X509Certificate
name|get
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
name|void
name|close
parameter_list|()
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

