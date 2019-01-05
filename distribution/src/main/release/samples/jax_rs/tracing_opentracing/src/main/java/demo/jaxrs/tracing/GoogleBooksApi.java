begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxrs
operator|.
name|tracing
package|;
end_package

begin_import
import|import
name|feign
operator|.
name|Headers
import|;
end_import

begin_import
import|import
name|feign
operator|.
name|Param
import|;
end_import

begin_import
import|import
name|feign
operator|.
name|RequestLine
import|;
end_import

begin_import
import|import
name|feign
operator|.
name|Response
import|;
end_import

begin_interface
specifier|public
interface|interface
name|GoogleBooksApi
block|{
annotation|@
name|RequestLine
argument_list|(
literal|"GET /books/v1/volumes?q={q}"
argument_list|)
annotation|@
name|Headers
argument_list|(
literal|"Accept: application/json"
argument_list|)
name|Response
name|search
parameter_list|(
annotation|@
name|Param
argument_list|(
literal|"q"
argument_list|)
name|String
name|query
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

