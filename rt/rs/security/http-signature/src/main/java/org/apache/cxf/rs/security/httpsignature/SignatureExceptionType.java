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
name|httpsignature
package|;
end_package

begin_enum
specifier|public
enum|enum
name|SignatureExceptionType
block|{
name|MISSING_SIGNATURE_HEADER
block|,
name|INVALID_SIGNATURE_HEADER
block|,
name|FAILED_TO_VERIFY_SIGNATURE
block|,
name|INVALID_DATA_TO_VERIFY_SIGNATURE
block|,
name|DIGEST_FAILURE
block|,
name|MISSING_DIGEST
block|,
name|DIFFERENT_DIGESTS
block|,
name|MULTIPLE_SIGNATURE_HEADERS
block|,
name|DIFFERENT_ALGORITHMS
block|}
end_enum

end_unit

