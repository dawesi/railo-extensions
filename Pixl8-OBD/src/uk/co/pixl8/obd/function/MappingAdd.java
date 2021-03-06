package uk.co.pixl8.obd.function;

import uk.co.pixl8.obd.tag.TagMapping;
import lucee.commons.io.res.Resource;
import lucee.loader.engine.CFMLEngineFactory;
import lucee.loader.util.Util;
import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;
import lucee.runtime.ext.function.Function;
import lucee.runtime.util.Cast;
import lucee.runtime.util.Creation;
import lucee.runtime.util.Excepton;

public class MappingAdd implements Function {

	private static final long serialVersionUID = 6833015099970494707L;

	public static boolean call(PageContext pc, String logicalPath) throws PageException{
		return call(pc, logicalPath, null, null);
	}

	public static boolean call(PageContext pc, String logicalPath, String directory) throws PageException{
		return call(pc, logicalPath, directory, null);
	}
	
	public static boolean call(PageContext pc, String logicalPath, String directory, String archive) throws PageException{
		boolean hasDirectoryPath=!Util.isEmpty(directory);
		boolean hasArchive=!Util.isEmpty(archive);
		
		// check attributes
		int notEmptyCount=(hasDirectoryPath?1:0)+(hasArchive?1:0);
		if(notEmptyCount==0)
			throw exp().createApplicationException("You have to define at least one of the following arguments: directory or archive");
		if(notEmptyCount>1)
			throw exp().createApplicationException("You can only define one of the following arguments: directory or archive");
		if(hasArchive)
			throw exp().createApplicationException("argument archive is not supported yet!");
		
		// get physical or archive Resource
		Resource physical=null;
		Resource _archive=null;
		if(hasDirectoryPath) {
			physical = creator().createResource(directory, false);
			
		}
		else /* archive */ {
			_archive = creator().createResource(archive, true);
		}
		TagMapping.addMapping(pc, caster(), logicalPath, physical, _archive);
		
		return true;
	}

	private static Cast caster() {
		return CFMLEngineFactory.getInstance().getCastUtil();
	}

	private static Creation creator() {
		return CFMLEngineFactory.getInstance().getCreationUtil();
	}

	private static Excepton exp() {
		return CFMLEngineFactory.getInstance().getExceptionUtil();
	}
}
