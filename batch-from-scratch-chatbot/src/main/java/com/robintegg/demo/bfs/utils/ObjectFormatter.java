package com.robintegg.demo.bfs.utils;

import java.util.List;

public class ObjectFormatter {

	public static String format( Object obj ) {
		return format( obj, 0 );
	}

	private static String format( Object obj, int indentLevel ) {
		if ( obj == null ) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();
		String indent = "  ".repeat( indentLevel );

		// Handle basic types or other objects
		if ( obj instanceof List<?> ) {
			sb.append( "[\n" );
			List<?> list = (List<?>) obj;
			for ( int i = 0; i < list.size(); i++ ) {
				sb.append( indent )
						.append( "  " )
						.append( format( list.get( i ), indentLevel + 1 ) );
				if ( i != list.size() - 1 ) {
					sb.append( "," );
				}
				sb.append( "\n" );
			}
			sb.append( indent ).append( "]" );
		} else if ( obj instanceof String ) {
			sb.append( "'" ).append( obj ).append( "'" );
		} else if ( obj.getClass().isRecord() ) {
			// Format Java records
			sb.append( obj.getClass().getSimpleName() ).append( "{\n" );
			var fields = obj.getClass().getDeclaredFields();
			for ( var field : fields ) {
				field.setAccessible( true );
				try {
					sb.append( indent )
							.append( "  " )
							.append( field.getName() )
							.append( "=" )
							.append( format( field.get( obj ), indentLevel + 1 ) );
				} catch ( IllegalAccessException e ) {
					sb.append( indent )
							.append( "  " )
							.append( field.getName() )
							.append( "=access denied" );
				}
				sb.append( "\n" );
			}
			sb.append( indent ).append( "}" );
		} else {
			sb.append( obj.toString() );
		}

		return sb.toString();
	}

}
