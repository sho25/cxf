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
name|openapi
operator|.
name|server
package|;
end_package

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|OpenAPIDefinition
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|info
operator|.
name|Info
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|info
operator|.
name|License
import|;
end_import

begin_class
annotation|@
name|OpenAPIDefinition
argument_list|(
name|info
operator|=
annotation|@
name|Info
argument_list|(
name|title
operator|=
literal|"Sample API"
argument_list|,
name|version
operator|=
literal|"1.0.0"
argument_list|,
name|description
operator|=
literal|"A sample API"
argument_list|,
name|license
operator|=
annotation|@
name|License
argument_list|(
name|name
operator|=
literal|"Apache 2.0 License"
argument_list|,
name|url
operator|=
literal|"https://www.apache.org/licenses/LICENSE-2.0"
argument_list|)
argument_list|)
argument_list|)
specifier|public
specifier|final
class|class
name|Metadata
block|{
specifier|private
name|Metadata
parameter_list|()
block|{     }
block|}
end_class

end_unit

